package com.example.client.screens.myorders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.client.screens.myorders.core.getOrdersCountText
import com.example.client.screens.myorders.uicomponents.EmptyOrdersState
import com.example.client.screens.myorders.uicomponents.ErrorState
import com.example.client.screens.myorders.uicomponents.LoadingState
import com.example.client.screens.myorders.uicomponents.OrderCard
import com.example.client.screens.myorders.uicomponents.OrdersStatistics
import com.example.client.screens.neworder.viewmodels.OrderViewModel
import com.example.uikit.uikit.ClientBottomNavigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    navController: NavHostController,
    viewModel: OrderViewModel = hiltViewModel(),
    onOrderClick: (String) -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // ✅ ИСПРАВЛЕНО: Ошибка показывается как Snackbar, а не блокирует экран
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Мои заказы")
                        Text(
                            text = "${orders.size} ${getOrdersCountText(orders.size)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
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
            ClientBottomNavigation(navController = navController)
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                // ✅ ИСПРАВЛЕНО: Показываем загрузку ТОЛЬКО при первой загрузке
                isLoading && orders.isEmpty() -> {
                    LoadingState()
                }
                // ✅ ИСПРАВЛЕНО: Пустое состояние — только если реально нет заказов И не загружаем
                !isLoading && orders.isEmpty() -> {
                    EmptyOrdersState()
                }
                // ✅ ИСПРАВЛЕНО: Показываем заказы ВСЕГДА, даже если есть ошибка (она в Snackbar)
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            OrdersStatistics(orders = orders)
                        }

                        items(
                            items = orders,
                            key = { it.id }
                        ) { order ->
                            OrderCard(
                                order = order,
                                onClick = { onOrderClick(order.id) }
                            )
                        }
                    }
                }
            }


        }
    }
}