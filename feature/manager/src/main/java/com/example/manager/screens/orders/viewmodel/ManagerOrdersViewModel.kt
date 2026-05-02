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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManagerOrdersViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository
): ViewModel() {

    private val _selectedFilter = MutableStateFlow<OrderFilter>(OrderFilter.ALL)
    val selectedFilter: StateFlow<OrderFilter> = _selectedFilter.asStateFlow()



    val orders: StateFlow<List<Order>> = _selectedFilter
        .flatMapLatest { filter ->
            when (filter) {
                OrderFilter.ALL -> repository.getAllOrders()
                OrderFilter.NEW -> repository.getOrdersByStatus("PENDING")
                OrderFilter.IN_PROGRESS -> repository.getOrdersByStatus("IN_PROGRESS")
                OrderFilter.COMPLETED -> repository.getOrdersByStatus("COMPLETED")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setFilter(filter: OrderFilter) {
        _selectedFilter.value = filter
    }

    fun updateStatus(orderId: String, status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateOrderStatus(orderId, status)
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

}