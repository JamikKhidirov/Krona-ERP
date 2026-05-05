package com.example.manager.screens.userdetailscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.uikit.screens.uikit.AddressCard
import com.example.manager.screens.userdetailscreen.uikit.ClientOrderCard
import com.example.manager.screens.userdetailscreen.uikit.ClientStatisticsCard
import com.example.manager.screens.userdetailscreen.uikit.ContactInfoCard
import com.example.manager.screens.userdetailscreen.uikit.EmptyOrdersPlaceholder
import com.example.uikit.screens.uikit.EmptyState
import com.example.manager.screens.userdetailscreen.uikit.NotesCard
import com.example.manager.screens.userdetailscreen.uikit.ProfileHeader
import com.example.manager.screens.userdetailscreen.uikit.RegistrationDateCard
import com.example.manager.screens.userdetailscreen.uikit.UidCard
import com.example.manager.screens.userdetailscreen.viewmodel.UserDetailViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    clientId: String,
    navController: NavHostController,
    onBackClick: () -> Unit,
    onOrderClick: (String) -> Unit,
    onCallClick: (String) -> Unit = {},
    onMessageClick: (String) -> Unit = {},
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val client by viewModel.client.collectAsState ()
    val orders by viewModel.clientOrders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(clientId) {
        viewModel.loadClientDetails(clientId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль клиента") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Редактировать */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6366F1),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                client == null -> {
                    EmptyState(message = "Клиент не найден")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Аватар + ФИО + Роль
                        item {
                            ProfileHeader(
                                client = client!!,
                                onCallClick = onCallClick,
                                onMessageClick = onMessageClick
                            )
                        }

                        // Статистика
                        item {
                            ClientStatisticsCard(
                                totalOrders = client!!.orderCount,
                                activeOrders = client!!.activeOrderCount,
                                totalSpent = client!!.totalSpent,
                                furnitureTypes = client!!.furnitureTypes,
                                lastOrderDate = client!!.lastOrderDate
                            )
                        }

                        // Контактная информация
                        item {
                            ContactInfoCard(client = client!!)
                        }

                        // Адрес
                        if (client!!.address.isNotBlank()) {
                            item {
                                AddressCard(address = client!!.address)
                            }
                        }

                        // Заметки
                        if (client!!.notes.isNotBlank()) {
                            item {
                                NotesCard(notes = client!!.notes)
                            }
                        }

                        // UID (для менеджера, скрыто/серым)
                        item {
                            UidCard(uid = client!!.uid)
                        }

                        // Заголовок заказов
                        item {
                            Text(
                                text = "История заказов",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }

                        // Список заказов
                        if (orders.isEmpty()) {
                            item {
                                EmptyOrdersPlaceholder()
                            }
                        } else {
                            items(
                                items = orders,
                                key = { it.id }
                            ) { order ->
                                ClientOrderCard(
                                    order = order,
                                    onClick = { onOrderClick(order.id) }
                                )
                            }
                        }

                        // Дата регистрации
                        item {
                            RegistrationDateCard(date = client!!.createdAt)
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            error?.let {
                LaunchedEffect(it) {
                    // Snackbar или Toast
                    viewModel.clearError()
                }
            }
        }
    }
}