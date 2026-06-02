package com.example.manager.screens.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manager.repository.ManagerOrdersRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ManagerOrdersRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _chatThreads = MutableStateFlow<List<ChatThread>>(emptyList())
    val chatThreads: StateFlow<List<ChatThread>> = _chatThreads.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadChatThreads() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            repository.getChatThreads(userId)
                .onSuccess { threads ->
                    _chatThreads.value = threads.sortedByDescending { it.lastMessageTime }
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка загрузки чатов"
                }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
