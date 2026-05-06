package com.example.uikit.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uikit.Client
import com.example.uikit.core.restartApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    fun loadCurrentUser() {
        val uid = auth.currentUser?.uid ?: run {
            _error.value = "Пользователь не авторизован"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()
                    .toObject(Client::class.java)
                    ?.let { _client.value = it.copy(id = uid, uid = uid) }
                    ?: run { _error.value = "Профиль не найден" }
            } catch (e: Exception) {
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun updateProfile(updatedClient: Client) {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestore.collection("users")
                    .document(uid)
                    .set(updatedClient)
                    .await()
                _client.value = updatedClient
            } catch (e: Exception) {
                _error.value = "Ошибка обновления: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        auth.signOut()
        _isLoggedOut.value = true
    }

    fun deleteAccount() {
        val user = auth.currentUser ?: return
        val uid = user.uid

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Удаляем фото из Storage
                _client.value?.avatarUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    try {
                        storage.getReferenceFromUrl(url).delete().await()
                    } catch (_: Exception) { }
                }

                // Удаляем документ из Firestore
                firestore.collection("users").document(uid).delete().await()

                // Удаляем заказы пользователя (опционально)
                val orders = firestore.collection("orders")
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()
                orders.documents.forEach { it.reference.delete().await() }

                // Удаляем аккаунт Firebase Auth
                user.delete().await()

                _isLoggedOut.value = true
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}. Возможно, нужно повторно войти."
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}


// ✅ Sealed class для навигации
sealed class NavigationEvent {
    object NavigateToAuth : NavigationEvent()
    object RestartApp : NavigationEvent()
}