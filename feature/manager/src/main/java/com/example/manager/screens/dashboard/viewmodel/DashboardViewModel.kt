package com.example.manager.screens.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manager.data.Order
import com.example.manager.repository.ManagerOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class DashboardState(
    val totalOrders: Int = 0,
    val pendingOrders: Int = 0,
    val inProgressOrders: Int = 0,
    val completedOrders: Int = 0,
    val cancelledOrders: Int = 0,
    val overdueOrders: Int = 0,
    val monthlyProfit: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getAllOrders().collect { orders ->
                val now = System.currentTimeMillis()
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val monthStart = cal.timeInMillis

                var monthlyProfit = 0.0
                var overdue = 0

                orders.forEach { order ->
                    if (order.createdAt >= monthStart && order.status in listOf("COMPLETED", "PAID")) {
                        monthlyProfit += order.budget.extractNumber().toDouble()
                    }

                    if (order.deadlineTimestamp > 0 && order.deadlineTimestamp < now &&
                        order.status !in listOf("COMPLETED", "PAID", "CANCELLED")) {
                        overdue++
                    }
                }

                _state.value = DashboardState(
                    totalOrders = orders.size,
                    pendingOrders = orders.count { it.status == "PENDING" },
                    inProgressOrders = orders.count {
                        it.status in listOf("ASSIGNED", "IN_PROGRESS", "READY", "DELIVERING")
                    },
                    completedOrders = orders.count { it.status in listOf("COMPLETED", "PAID") },
                    cancelledOrders = orders.count { it.status == "CANCELLED" },
                    overdueOrders = overdue,
                    monthlyProfit = monthlyProfit,
                    isLoading = false
                )
            }
        }
    }
}

private fun String.extractNumber(): Long {
    return this.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0
}
