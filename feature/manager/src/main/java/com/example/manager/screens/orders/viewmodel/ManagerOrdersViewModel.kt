package com.example.manager.screens.orders.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manager.data.Order
import com.example.manager.data.OrderFilter
import com.example.manager.repository.ManagerOrdersRepository
import com.example.network.data.OrderStatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerOrdersViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<OrderFilter>(OrderFilter.ALL)
    val selectedFilter: StateFlow<OrderFilter> = _selectedFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    val currentManagerId: String
        get() = auth.currentUser?.uid ?: ""

    // ✅ ИСПРАВЛЕНО: Используем unsorted версию если нет индекса
    val allOrders: StateFlow<List<Order>> = repository.getAllOrdersUnsorted()  // <-- БЕЗ ИНДЕКСА
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unassignedOrders: StateFlow<List<Order>> = repository.getUnassignedOrdersSimple()  // <-- БЕЗ ИНДЕКСА
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myOrders: StateFlow<List<Order>> = repository.getMyOrdersSimple(currentManagerId)  // <-- БЕЗ ИНДЕКСА
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ✅ ИСПРАВЛЕНО: Фильтрация безопасная
    val orders: StateFlow<List<Order>> = combine(
        allOrders,
        _selectedFilter
    ) { orders, filter ->
        when (filter) {
            OrderFilter.ALL -> orders
            OrderFilter.NEW -> orders.filter { it.status == "PENDING" }
            OrderFilter.MY_ORDERS -> orders.filter { it.managerId == currentManagerId }
            OrderFilter.IN_PROGRESS -> orders.filter {
                it.status in listOf("ASSIGNED", "IN_PROGRESS")
            }
            OrderFilter.READY -> orders.filter { it.status == "READY" }
            OrderFilter.COMPLETED -> orders.filter { it.status == "COMPLETED" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ==================== ДЕЙСТВИЯ ====================

    fun assignOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.assignOrder(orderId, currentManagerId, getManagerName())
                .onSuccess {
                    _successMessage.value = "Заказ взят в работу!"
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка назначения"
                }

            _isLoading.value = false
        }
    }

    fun updateStatus(orderId: String, newStatus: OrderStatus, comment: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.updateOrderStatus(orderId, newStatus, comment)
                .onSuccess {
                    _successMessage.value = "Статус обновлён: ${getStatusLabel(newStatus)}"
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка обновления статуса"
                }

            _isLoading.value = false
        }
    }

    fun cancelOrder(orderId: String, reason: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateOrderStatus(orderId, OrderStatus.CANCELLED, reason)
                .onSuccess {
                    _successMessage.value = "Заказ отменён"
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка отмены"
                }
            _isLoading.value = false
        }
    }

    fun setFilter(filter: OrderFilter) {
        _selectedFilter.value = filter
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    private fun getManagerName(): String {
        return auth.currentUser?.displayName ?: "Менеджер"
    }

    private fun getStatusLabel(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Ожидает"
            OrderStatus.ASSIGNED -> "Назначен"
            OrderStatus.IN_PROGRESS -> "В работе"
            OrderStatus.READY -> "Готов"
            OrderStatus.DELIVERING -> "Доставляется"
            OrderStatus.COMPLETED -> "Выполнен"
            OrderStatus.CANCELLED -> "Отменён"
        }
    }
}

enum class OrderFilter {
    ALL, NEW, MY_ORDERS, IN_PROGRESS, READY, COMPLETED
}