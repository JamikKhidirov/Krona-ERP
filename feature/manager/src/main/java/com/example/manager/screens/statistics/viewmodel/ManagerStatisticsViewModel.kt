package com.example.manager.screens.statistics.viewmodel

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
import java.util.Locale
import javax.inject.Inject

data class StatusStat(
    val status: String,
    val label: String,
    val count: Int,
    val percentage: Float,
    val color: Long
)

data class MonthlyRevenue(
    val month: String,
    val revenue: Double,
    val count: Int
)

data class PriorityStat(
    val priority: String,
    val label: String,
    val count: Int,
    val color: Long
)

data class PaymentStat(
    val status: String,
    val label: String,
    val count: Int,
    val total: Double
)

data class StatisticsState(
    val totalOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val totalProfit: Double = 0.0,
    val averageMargin: Double = 0.0,
    val averageOrderValue: Double = 0.0,
    val overdueCount: Int = 0,
    val statusStats: List<StatusStat> = emptyList(),
    val monthlyRevenue: List<MonthlyRevenue> = emptyList(),
    val priorityStats: List<PriorityStat> = emptyList(),
    val paymentStats: List<PaymentStat> = emptyList(),
    val isClientApp: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class ManagerStatisticsViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getAllOrders().collect { orders ->

                val now = System.currentTimeMillis()
                val cal = Calendar.getInstance()

                val statusLabels = mapOf(
                    "PENDING" to "Ожидает",
                    "ASSIGNED" to "Назначен",
                    "IN_PROGRESS" to "В работе",
                    "READY" to "Готов",
                    "DELIVERING" to "Доставка",
                    "COMPLETED" to "Завершён",
                    "PAID" to "Оплачен",
                    "CANCELLED" to "Отменён"
                )

                val statusColors = mapOf(
                    "PENDING" to 0xFFF59E0B,
                    "ASSIGNED" to 0xFF3B82F6,
                    "IN_PROGRESS" to 0xFF8B5CF6,
                    "READY" to 0xFF10B981,
                    "DELIVERING" to 0xFF06B6D4,
                    "COMPLETED" to 0xFF059669,
                    "PAID" to 0xFF059669,
                    "CANCELLED" to 0xFFEF4444
                )

                val priorityLabels = mapOf(
                    "LOW" to "Низкий",
                    "NORMAL" to "Средний",
                    "HIGH" to "Высокий",
                    "URGENT" to "Срочный"
                )

                val priorityColors = mapOf(
                    "LOW" to 0xFF94A3B8,
                    "NORMAL" to 0xFF3B82F6,
                    "HIGH" to 0xFFF59E0B,
                    "URGENT" to 0xFFEF4444
                )

                val paymentLabels = mapOf(
                    "UNPAID" to "Не оплачен",
                    "PARTIAL" to "Частично",
                    "PAID" to "Оплачен",
                    "OVERPAID" to "Переплата"
                )

                val total = orders.size

                val statusStats = listOf(
                    "PENDING", "ASSIGNED", "IN_PROGRESS", "READY",
                    "DELIVERING", "COMPLETED", "PAID", "CANCELLED"
                ).map { status ->
                    val count = orders.count { it.status == status }
                    StatusStat(
                        status = status,
                        label = statusLabels[status] ?: status,
                        count = count,
                        percentage = if (total > 0) (count * 100f) / total else 0f,
                        color = statusColors[status] ?: 0xFF64748B
                    )
                }

                val priorityStats = listOf("LOW", "NORMAL", "HIGH", "URGENT").map { priority ->
                    val count = orders.count { it.priority == priority }
                    PriorityStat(
                        priority = priority,
                        label = priorityLabels[priority] ?: priority,
                        count = count,
                        color = priorityColors[priority] ?: 0xFF64748B
                    )
                }

                val paymentStats = listOf("UNPAID", "PARTIAL", "PAID", "OVERPAID").map { status ->
                    val matchingOrders = orders.filter { it.paymentStatus == status }
                    PaymentStat(
                        status = status,
                        label = paymentLabels[status] ?: status,
                        count = matchingOrders.size,
                        total = matchingOrders.sumOf { it.budget.extractNumber().toDouble() }
                    )
                }

                val monthlyRevenue = buildMonthlyRevenue(orders)

                val totalRevenue = orders.sumOf { it.budget.extractNumber().toDouble() }
                val totalProfit = orders.sumOf {
                    val budgetVal = it.budget.extractNumber()
                    val costVal = it.costPrice.extractNumber()
                    val deliveryVal = it.deliveryCost.extractNumber()
                    val assemblyVal = it.assemblyCost.extractNumber()
                    (budgetVal - costVal - deliveryVal - assemblyVal).toDouble()
                }

                val completedOrders = orders.filter { it.status == "COMPLETED" }
                val completedRevenue = completedOrders.sumOf { it.budget.extractNumber().toDouble() }

                val averageMargin = if (completedOrders.isNotEmpty()) {
                    completedOrders.map { it.getMarginPercent().toDouble() }.average()
                } else 0.0

                val averageOrderValue = if (total > 0) totalRevenue / total else 0.0

                val overdueCount = orders.count {
                    it.deadlineTimestamp > 0 && it.deadlineTimestamp < now &&
                            it.status !in listOf("COMPLETED", "PAID", "CANCELLED")
                }

                val isClientApp = orders.any { it.userId == "" }

                _state.value = StatisticsState(
                    totalOrders = total,
                    totalRevenue = totalRevenue,
                    totalProfit = totalProfit,
                    averageMargin = averageMargin,
                    averageOrderValue = averageOrderValue,
                    overdueCount = overdueCount,
                    statusStats = statusStats,
                    monthlyRevenue = monthlyRevenue,
                    priorityStats = priorityStats,
                    paymentStats = paymentStats,
                    isClientApp = isClientApp,
                    isLoading = false
                )
            }
        }
    }

    private fun buildMonthlyRevenue(orders: List<Order>): List<MonthlyRevenue> {
        val cal = Calendar.getInstance()
        val result = mutableListOf<MonthlyRevenue>()
        val monthNames = arrayOf(
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
            "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        )

        for (i in 11 downTo 0) {
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.MONTH, -i)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val monthStart = cal.timeInMillis

            cal.add(Calendar.MONTH, 1)
            val monthEnd = cal.timeInMillis

            val monthOrders = orders.filter {
                it.createdAt in monthStart until monthEnd
            }

            val revenue = monthOrders.sumOf { it.budget.extractNumber().toDouble() }

            result.add(
                MonthlyRevenue(
                    month = monthNames[cal.get(Calendar.MONTH)],
                    revenue = revenue,
                    count = monthOrders.size
                )
            )
        }

        return result
    }
}

private fun String.extractNumber(): Long {
    return this.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0
}
