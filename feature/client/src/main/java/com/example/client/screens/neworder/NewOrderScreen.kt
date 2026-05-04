package com.example.client.screens.neworder

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.client.uicomponents.HeaderSection
import com.example.client.uicomponents.InputFieldsSection
import com.example.client.data.ProductType
import com.example.client.screens.neworder.uicomponents.CommentSection
import com.example.client.screens.neworder.uicomponents.DimensionsSection
import com.example.client.screens.neworder.uicomponents.PhotoUploadSection
import com.example.client.screens.neworder.viewmodels.OrderViewModel
import com.example.client.uicomponents.MasterInfoCard
import com.example.client.uicomponents.PhotoUploadSection
import com.example.client.uicomponents.PortfolioSection
import com.example.client.uicomponents.ProductTypeGrid
import com.example.client.uicomponents.SubmitButton
import com.example.client.uicomponents.bottombar.ClientBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow


// NewOrderScreen.kt



@Composable
fun NewOrderScreen(
    navController: NavHostController,
    viewModel: OrderViewModel = hiltViewModel(),
    onNavigateToOrders: () -> Unit = {}
) {
    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("50 000") }
    var widthCm by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var depthCm by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") } // Комментарий к заказу

    // Список выбранных фото
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val productTypes = remember {
        listOf(
            ProductType(1, "Стул / Кресло", Icons.Default.Home),
            ProductType(2, "Стол", Icons.Default.Build),
            ProductType(3, "Кровать", Icons.Default.Star),
            ProductType(4, "Шкаф", Icons.Default.Info),
            ProductType(5, "Кухня", Icons.Default.ShoppingCart),
            ProductType(6, "Другое", Icons.Default.MoreVert)
        )
    }
    var selectedTypeId by remember { mutableIntStateOf(1) }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Лончер для выбора фото из галереи (Photo Picker API) [^1^]
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        selectedImages = uris
    }

    // Показ ошибки
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        bottomBar = {
            // Нижний бар
            ClientBottomNavigation(navController = navController)
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF6366F1))
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { HeaderSection() }

                item {
                    ProductTypeGrid(
                        items = productTypes,
                        selectedId = selectedTypeId,
                        onTypeSelected = { id -> selectedTypeId = id }
                    )
                }

                item {
                    InputFieldsSection(
                        price = budget,
                        description = description,
                        onValueChanePrice = { budget = it },
                        onVAlueChangeDescription = { description = it }
                    )
                }

                // Поля для размеров (ширина, высота, глубина в см)
                item {
                    DimensionsSection(
                        width = widthCm,
                        height = heightCm,
                        depth = depthCm,
                        onWidthChange = { widthCm = it },
                        onHeightChange = { heightCm = it },
                        onDepthChange = { depthCm = it }
                    )
                }

                // Комментарий к заказу
                item {
                    CommentSection(
                        comment = comment,
                        onCommentChange = { comment = it }
                    )
                }

                // Секция загрузки фото
                item {
                    PhotoUploadSection(
                        selectedImages = selectedImages,
                        onAddPhoto = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        onRemovePhoto = { uri ->
                            selectedImages = selectedImages - uri
                        }
                    )
                }

                item {
                    SubmitButton(
                        isLoading = isLoading,
                        onClick = {
                            val selectedType = productTypes.find { it.id == selectedTypeId }
                            viewModel.createOrder(
                                productTypeId = selectedTypeId,
                                productTypeName = selectedType?.name ?: "",
                                description = description,
                                budget = budget,
                                widthCm = widthCm,
                                heightCm = heightCm,
                                depthCm = depthCm,
                                imageUris = selectedImages
                            )
                        }
                    )
                }

                item { MasterInfoCard() }
                item { PortfolioSection() }
            }
        }
    }
}




@Composable
fun SubmitButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = "Создать заказ",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



