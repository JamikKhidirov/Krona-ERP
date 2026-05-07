package com.example.manager.screens.orders




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.manager.data.Order
import com.example.manager.data.OrderFilter
import com.example.manager.uikit.bottombar.ManagerBottomNavigation
import com.example.manager.screens.orders.uikit.EmptyOrdersState
import com.example.manager.screens.orders.uikit.FilterChips
import com.example.manager.screens.orders.uikit.ManagerOrderCard
import com.example.manager.screens.orders.viewmodel.ManagerOrdersViewModel
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerOrdersScreen(
    navController: NavHostController,
    viewModel: ManagerOrdersViewModel = hiltViewModel(),
    onOrderClick: (String) -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    val unassigned by viewModel.unassignedOrders.collectAsState()
    val myOrders by viewModel.myOrders.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.successMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Показ ошибок и успеха
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearError()
        }
    }
    LaunchedEffect(success) {
        success?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Заказы",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Новых: ${unassigned.size} | Моих: ${myOrders.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6366F1),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            ManagerBottomNavigation(navController = navController)
        },
        containerColor = Color(0xFFF5F7FA)
    ) { innerPadding ->
        // ✅ ИСПРАВЛЕНО: innerPadding применяем один раз на корневом Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // ← Только здесь!
        ) {
            // Фильтры — без дополнительного отступа сверху
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )

            // Контент
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp) // ← Горизонтальные отступы только для списка
            ) {
                when {
                    isLoading && orders.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFF6366F1)
                        )
                    }
                    orders.isEmpty() -> {
                        EmptyOrdersState()
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = 12.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = orders,
                                key = { it.id }
                            ) { order ->
                                ManagerOrderCard(
                                    order = order,
                                    currentManagerId = viewModel.currentManagerId,
                                    onClick = { onOrderClick(order.id) },
                                    onAssign = { viewModel.assignOrder(order.id) },
                                    onUpdateStatus = { status ->
                                        viewModel.updateStatus(order.id, status)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun ManagerOrdersScreenPreview() {
    MaterialTheme {
        val previewOrders = listOf(
            Order(
                id = "abc123",
                title = "Шкаф-купе встроенный",
                clientName = "ИП Иванов",
                material = "ЛДСП Egger",
                budget = "125 000",
                status = "IN_PROGRESS",
                imageUrls = emptyList()
            ),
            Order(
                id = "def456",
                title = "Кухонный гарнитур «Сканди»",
                clientName = "Петров А.",
                material = "МДФ Эмаль",
                budget = "245 000",
                status = "PENDING",
                imageUrls = emptyList()
            ),
            Order(
                id = "ghi789",
                title = "Стол обеденный дуб",
                clientName = "ООО «Мебель Плюс»",
                material = "Массив дуба",
                budget = "45 000",
                status = "COMPLETED",
                imageUrls = emptyList()
            )
        )

        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))) {
            FilterChips(
                selectedFilter = OrderFilter.ALL,
                onFilterSelected = {}
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(previewOrders) { order ->
                    ManagerOrderCard(
                        order = order,
                        onClick = {},
                        currentManagerId = TODO(),
                        onAssign = TODO(),
                        onUpdateStatus = TODO()
                    )
                }
            }
        }
    }
}