package com.example.client.screens.neworder.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.data.order.Order
import com.example.client.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
            // ✅ ИСПРАВЛЕНО: Очищаем ошибку ПЕРЕД загрузкой
            _error.value = null

            repository.getUserOrders(userId)
                .catch { e ->
                    _error.value = when (e) {
                        is FirebaseFirestoreException -> "Ошибка загрузки: ${e.message}"
                        is CancellationException -> {
                            _isLoading.value = false
                            return@catch
                        }
                        else -> "Ошибка: ${e.message}"
                    }
                    _isLoading.value = false
                }
                .collect { ordersList ->
                    // ✅ ИСПРАВЛЕНО: Очищаем ошибку при успехе!
                    _error.value = null
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
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _error.value = "Не авторизован"
            return
        }

        if (description.isBlank()) {
            _error.value = "Введите описание"
            return
        }

        viewModelScope.launch {
            _isCreating.value = true
            _error.value = null
            _orderCreated.value = false

            val order = Order(
                userId = userId,
                productTypeId = productTypeId,
                productTypeName = productTypeName,
                description = description.trim(),
                budget = budget.trim(),
                widthCm = widthCm.trim(),
                heightCm = heightCm.trim(),
                depthCm = depthCm.trim(),
                comment = comment.trim()
            )

            repository.createOrder(order, imageUris)
                .onSuccess { orderId ->
                    _orderCreated.value = true
                    // ✅ ИСПРАВЛЕНО: Очищаем ошибку при успехе
                    _error.value = null
                    loadUserOrders()
                }
                .onFailure { e ->
                    _error.value = when (e) {
                        is StorageException -> "Ошибка фото: ${e.message}"
                        is FirebaseFirestoreException -> "Ошибка сохранения: ${e.message}"
                        else -> "Ошибка: ${e.message}"
                    }
                }

            _isCreating.value = false
        }
    }

    fun resetOrderCreated() {
        _orderCreated.value = false
    }

    fun refreshOrders() {
        loadUserOrders()
    }

    // ✅ ИСПРАВЛЕНО: Публичный метод для очистки ошибки
    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ordersJob?.cancel()
    }
}