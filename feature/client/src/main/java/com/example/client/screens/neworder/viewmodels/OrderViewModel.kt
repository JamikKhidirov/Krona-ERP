package com.example.client.screens.neworder.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.data.order.Order
import com.example.client.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val auth: FirebaseAuth // Инжектим вместо getInstance()
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Для отслеживания успешного создания
    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated.asStateFlow()

    private var ordersJob: Job? = null

    init {
        loadUserOrders()
    }

    private fun loadUserOrders() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _error.value = "Пользователь не авторизован"
            return
        }

        ordersJob?.cancel()
        ordersJob = viewModelScope.launch {
            _isLoading.value = true
            repository.getUserOrders(userId)
                .catch { e ->
                    _error.value = "Ошибка загрузки: ${e.message}"
                    _isLoading.value = false
                }
                .collect { ordersList ->
                    _orders.value = ordersList
                    _isLoading.value = false
                }
        }
    }

    fun createOrder(
        productTypeId: Int,
        productTypeName: String,
        description: String,
        budget: String,
        widthCm: String,
        heightCm: String,
        depthCm: String,
        comment: String = "",
        imageUris: List<Uri>
    ) {
        val userId = auth.currentUser?.uid ?: run {
            _error.value = "Пользователь не авторизован"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _orderCreated.value = false

            val order = Order(
                userId = userId, // ВАЖНО: передаём userId!
                productTypeId = productTypeId,
                productTypeName = productTypeName,
                description = description,
                budget = budget,
                widthCm = widthCm,
                heightCm = heightCm,
                depthCm = depthCm,
                comment = comment
            )

            repository.createOrder(order, imageUris)
                .onSuccess { orderId ->
                    _error.value = null
                    _orderCreated.value = true
                    // Перезагружаем список
                    loadUserOrders()
                }
                .onFailure { e ->
                    _error.value = "Ошибка создания: ${e.message}"
                }

            _isLoading.value = false
        }
    }

    fun resetOrderCreated() {
        _orderCreated.value = false
    }

    fun refreshOrders() {
        loadUserOrders()
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ordersJob?.cancel()
    }
}