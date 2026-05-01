package com.example.client.screens.neworder.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.data.order.Order
import com.example.client.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUserOrders()
    }




    private fun loadUserOrders() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            repository.getUserOrders(userId)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { ordersList ->
                    _orders.value = ordersList
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
        imageUris: List<Uri>
    ) {
        val userId = auth.currentUser?.uid ?: run {
            _error.value = "Пользователь не авторизован"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val order = Order(
                userId = userId,
                productTypeId = productTypeId,
                productTypeName = productTypeName,
                description = description,
                budget = budget,
                widthCm = widthCm,
                heightCm = heightCm,
                depthCm = depthCm
            )

            repository.createOrder(order, imageUris)
                .onSuccess { orderId ->
                    _error.value = null
                    // Можно добавить навигацию или уведомление
                }
                .onFailure { e ->
                    _error.value = e.message
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}