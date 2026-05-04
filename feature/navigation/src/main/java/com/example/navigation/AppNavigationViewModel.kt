package com.example.navigation





import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.screens.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.network.UserProfile

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Loading)
    val navigationState: StateFlow<NavigationState> = _navigationState

    init {
        checkAuthState()
    }

    /**
     * Проверяем, авторизован ли пользователь и какая у него роль
     */
    private fun checkAuthState() {
        viewModelScope.launch {
            if (authRepository.isUserLoggedIn()) {
                authRepository.getCurrentUserProfile().fold(
                    onSuccess = { profile ->
                        _navigationState.value = when (profile.role) {
                            "Менеджер" -> NavigationState.Manager
                            else -> NavigationState.Client
                        }
                    },
                    onFailure = {
                        // Если профиль не найден — разлогиниваем
                        authRepository.signOut()
                        _navigationState.value = NavigationState.Auth
                    }
                )
            } else {
                _navigationState.value = NavigationState.Auth
            }
        }
    }

    /**
     * Вызывается после успешного входа/регистрации
     */
    fun onAuthSuccess(role: String) {
        _navigationState.value = when (role) {
            "Менеджер" -> NavigationState.Manager
            else -> NavigationState.Client
        }
    }

    /**
     * Выход из системы
     */
    fun logout() {
        authRepository.signOut()
        _navigationState.value = NavigationState.Auth
    }
}

sealed class NavigationState {
    object Loading : NavigationState()
    object Auth : NavigationState()
    object Client : NavigationState()
    object Manager : NavigationState()
}