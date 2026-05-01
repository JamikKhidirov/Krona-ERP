package com.example.client.screens.orderdetailscreen.viewmodel



import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.screens.orderdetailscreen.data.Order

import com.example.client.screens.orderdetailscreen.repository.OrderDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: OrderDetailRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadOrder()
    }


    private fun loadOrder() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getOrderById(orderId)
                .onSuccess { data ->
                    _order.value = data
                    _error.value = null
                }
                .onFailure {
                    _error.value = it.message
                }
            _isLoading.value = false
        }
    }

    fun refresh() = loadOrder()

}