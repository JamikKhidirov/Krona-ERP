package com.example.manager.screens.orders.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manager.data.Order
import com.example.manager.data.OrderFilter
import com.example.manager.repository.ManagerOrdersRepository
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
    private val repository: ManagerOrdersRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<OrderFilter>(OrderFilter.ALL)
    val selectedFilter: StateFlow<OrderFilter> = _selectedFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Все заказы — с полной защитой
    private val allOrders = try {
        repository.getAllOrders()
            .onStart {
                _isLoading.value = true
                android.util.Log.d("OrdersVM", "Начало загрузки")
            }
            .onEach { list ->
                _isLoading.value = false
                android.util.Log.d("OrdersVM", "Загружено: ${list.size}")
            }
            .catch { e ->
                _isLoading.value = false
                _error.value = "Ошибка загрузки: ${e.message}"
                android.util.Log.e("OrdersVM", "❌ Ошибка Flow: ${e.message}")
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    } catch (e: Exception) {
        android.util.Log.e("OrdersVM", "❌ Критическая ошибка: ${e.message}")
        MutableStateFlow(emptyList())
    }

    // Фильтрованные заказы — на клиенте
    val orders: StateFlow<List<Order>> = try {
        combine(allOrders, _selectedFilter) { orders, filter ->
            try {
                when (filter) {
                    OrderFilter.ALL -> orders
                    OrderFilter.NEW -> orders.filter { it.status.equals("PENDING", true) }
                    OrderFilter.IN_PROGRESS -> orders.filter { it.status.equals("IN_PROGRESS", true) }
                    OrderFilter.COMPLETED -> orders.filter { it.status.equals("COMPLETED", true) }
                }
            } catch (e: Exception) {
                android.util.Log.e("OrdersVM", "❌ Ошибка фильтра: ${e.message}")
                orders // При ошибке — показываем все
            }
        }.catch { e ->
            android.util.Log.e("OrdersVM", "❌ Ошибка combine: ${e.message}")
            emit(emptyList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    } catch (e: Exception) {
        android.util.Log.e("OrdersVM", "❌ Критическая ошибка combine: ${e.message}")
        MutableStateFlow(emptyList())
    }

    fun setFilter(filter: OrderFilter) {
        try {
            _selectedFilter.value = filter
            android.util.Log.d("OrdersVM", "Фильтр: $filter")
        } catch (e: Exception) {
            android.util.Log.e("OrdersVM", "❌ Ошибка смены фильтра: ${e.message}")
        }
    }

    fun updateStatus(orderId: String, status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.updateOrderStatus(orderId, status)
                    .onSuccess {
                        android.util.Log.d("OrdersVM", "✅ Статус обновлён")
                    }
                    .onFailure { error ->
                        _error.value = error.message
                        android.util.Log.e("OrdersVM", "❌ Ошибка: ${error.message}")
                    }
            } catch (e: Exception) {
                _error.value = "Критическая ошибка: ${e.message}"
                android.util.Log.e("OrdersVM", "❌ Крит: ${e.message}")
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}

enum class OrderFilter {
    ALL, NEW, IN_PROGRESS, COMPLETED
}