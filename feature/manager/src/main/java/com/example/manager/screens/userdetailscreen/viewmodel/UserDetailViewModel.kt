package com.example.manager.screens.userdetailscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.manager.data.Order
import com.example.manager.screens.clentscreen.repository.ClientRepository
import com.example.uikit.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.onSuccess


@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _clientOrders = MutableStateFlow<List<Order>>(emptyList())
    val clientOrders: StateFlow<List<Order>> = _clientOrders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var ordersJob: Job? = null

    fun loadClientDetails(clientId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Загружаем клиента
            clientRepository.getClientById(clientId)
                .onSuccess { client ->
                    _client.value = client
                    _error.value = null

                    // Подписываемся на заказы
                    loadClientOrders(clientId)
                }
                .onFailure { e ->
                    _error.value = e.message
                }

            _isLoading.value = false
        }
    }

    private fun loadClientOrders(clientId: String) {
        ordersJob?.cancel()
        ordersJob = viewModelScope.launch {
            clientRepository.getClientOrders(clientId)
                .catch { e ->
                    _error.value = "Ошибка загрузки заказов: ${e.message}"
                }
                .collect { orders ->
                    _clientOrders.value = orders
                }
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ordersJob?.cancel()
    }
}
