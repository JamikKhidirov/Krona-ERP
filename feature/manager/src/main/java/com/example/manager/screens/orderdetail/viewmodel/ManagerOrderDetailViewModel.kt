package com.example.manager.screens.orderdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manager.data.ChatMessage
import com.example.manager.data.Order
import com.example.manager.data.OrderHistoryItem
import com.example.manager.repository.ManagerOrdersRepository
import com.example.network.data.OrderStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerOrderDetailViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _history = MutableStateFlow<List<OrderHistoryItem>>(emptyList())
    val history: StateFlow<List<OrderHistoryItem>> = _history.asStateFlow()

    val currentManagerId: String
        get() = auth.currentUser?.uid ?: ""

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getOrderById(orderId)
                .onSuccess { data ->
                    _order.value = data
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка загрузки заказа"
                }

            _isLoading.value = false
        }
    }

    fun observeHistory(orderId: String) {
        viewModelScope.launch {
            repository.getOrderHistoryFlow(orderId)
                .collect { historyList ->
                    _history.value = historyList
                }
        }
    }

    fun assignOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.assignOrder(orderId, currentManagerId, getManagerName())
                .onSuccess {
                    _successMessage.value = "Заказ взят в работу!"
                    loadOrder(orderId)
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
                    _successMessage.value = "Статус обновлён"
                    loadOrder(orderId)
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка обновления статуса"
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun getChatMessages(orderId: String): Flow<List<ChatMessage>> {
        return repository.getChatMessages(orderId)
    }

    fun sendChatMessage(orderId: String, text: String) {
        viewModelScope.launch {
            repository.sendChatMessage(orderId, text)
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка отправки сообщения"
                }
        }
    }

    private fun getManagerName(): String {
        return auth.currentUser?.displayName ?: "Менеджер"
    }
}
