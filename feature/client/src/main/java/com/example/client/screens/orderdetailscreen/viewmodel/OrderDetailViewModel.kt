package com.example.client.screens.orderdetailscreen.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.screens.orderdetailscreen.data.ChatMessage
import com.example.client.screens.orderdetailscreen.data.Order
import com.example.client.screens.orderdetailscreen.data.StatusUpdate
import com.example.client.screens.orderdetailscreen.repository.OrderDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: OrderDetailRepository,
): ViewModel(){

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _history = MutableStateFlow<List<StatusUpdate>>(emptyList())
    val history: StateFlow<List<StatusUpdate>> = _history.asStateFlow()

    fun observeHistory(orderId: String) {
        viewModelScope.launch {
            repository.getOrderHistoryFlow(orderId)
                .collect { historyList ->
                    _history.value = historyList
                }
        }
    }

    fun loadOrder(orderId: String) {
        if (orderId.isEmpty()) {
            _error.value = "ID заказа пустой"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getOrderById(orderId)
                .onSuccess { data ->
                    _order.value = data
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка загрузки"
                }

            _isLoading.value = false
        }
    }

    fun refresh(orderId: String) = loadOrder(orderId)

    fun getChatMessages(orderId: String): Flow<List<ChatMessage>> {
        return repository.getChatMessages(orderId)
    }

    fun saveReview(orderId: String, rating: Int, review: String) {
        viewModelScope.launch {
            repository.saveReview(orderId, rating, review)
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка сохранения отзыва"
                }
        }
    }

    fun sendChatMessage(orderId: String, text: String) {
        viewModelScope.launch {
            repository.sendChatMessage(orderId, text)
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка отправки сообщения"
                }
        }
    }
}