package com.example.client.screens.neworder

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.client.data.ProductType
import com.example.client.screens.neworder.uicomponents.CommentSection
import com.example.client.screens.neworder.uicomponents.DimensionsSection
import com.example.client.screens.neworder.uicomponents.PhotoUploadSection
import com.example.client.screens.neworder.viewmodels.OrderViewModel
import com.example.client.uicomponents.DeliveryAddressSection
import com.example.client.uicomponents.FactoryAddress
import com.example.client.uicomponents.HeaderSection
import com.example.client.uicomponents.InputFieldsSection
import com.example.client.uicomponents.ProductTypeGrid
import com.example.uikit.uikit.ClientBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(
    navController: NavHostController,
    viewModel: OrderViewModel = hiltViewModel(),
    onNavigateToOrders: () -> Unit = {}
) {
    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var widthCm by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var depthCm by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedAddress by remember { mutableStateOf<FactoryAddress?>(null) }

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

    val isCreating by viewModel.isCreating.collectAsState()
    val error by viewModel.error.collectAsState()
    val orderCreated by viewModel.orderCreated.collectAsState()

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            selectedImages = selectedImages + photoUri!!
            photoUri = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        selectedImages = uris
    }

    val isFormValid = description.isNotBlank() && budget.isNotBlank() &&
            widthCm.isNotBlank() && heightCm.isNotBlank() &&
            selectedAddress != null

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(orderCreated) {
        if (orderCreated) {
            Toast.makeText(context, "Заказ создан!", Toast.LENGTH_SHORT).show()
            viewModel.resetOrderCreated()
            onNavigateToOrders()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Новый заказ", fontWeight = FontWeight.Bold)
                        Text(
                            "Заполните детали заказа",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            ClientBottomNavigation(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        HeaderSection()

                        Spacer(modifier = Modifier.height(16.dp))

                        ProductTypeGrid(
                            items = productTypes,
                            selectedId = selectedTypeId,
                            onTypeSelected = { selectedTypeId = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        InputFieldsSection(
                            price = budget,
                            description = description,
                            onValueChanePrice = { budget = it },
                            onVAlueChangeDescription = { description = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DimensionsSection(
                            width = widthCm,
                            height = heightCm,
                            depth = depthCm,
                            onWidthChange = { widthCm = it },
                            onHeightChange = { heightCm = it },
                            onDepthChange = { depthCm = it }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CommentSection(
                            comment = comment,
                            onCommentChange = { comment = it }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        PhotoUploadSection(
                            selectedImages = selectedImages,
                            onAddPhotoFromGallery = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            onAddPhotoFromCamera = {
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasCameraPermission) {
                                    val file = java.io.File(
                                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                        "krona_${System.currentTimeMillis()}.jpg"
                                    )
                                    photoUri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    cameraLauncher.launch(photoUri!!)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            onRemovePhoto = { uri ->
                                selectedImages = selectedImages - uri
                            }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DeliveryAddressSection(
                            selectedAddress = selectedAddress,
                            onAddressSelected = { selectedAddress = it }
                        )
                    }
                }

                Button(
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
                            comment = comment,
                            imageUris = selectedImages,
                            address = selectedAddress?.address ?: "",
                            city = selectedAddress?.city ?: ""
                        )
                    },
                    enabled = isFormValid && !isCreating,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color(0xFFC7D2FE)
                    )
                ) {
                    if (isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Создать заказ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isCreating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
