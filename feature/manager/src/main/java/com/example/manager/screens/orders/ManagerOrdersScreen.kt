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
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Заказы",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Управление производством",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6366F1),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            ManagerBottomNavigation(navController = navController)
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Фильтры
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )

            // Список заказов
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading && orders.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    orders.isEmpty() -> {
                        EmptyOrdersState()
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = orders,
                                key = { it.id }
                            ) { order ->
                                ManagerOrderCard(
                                    order = order,
                                    onClick = { onOrderClick(order.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Показ ошибки
        error?.let {
            LaunchedEffect(it) {
                // Можно добавить Snackbar
                viewModel.clearError()
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
                        onClick = {}
                    )
                }
            }
        }
    }
}