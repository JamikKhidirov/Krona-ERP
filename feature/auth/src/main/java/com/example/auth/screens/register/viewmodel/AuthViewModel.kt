package com.example.auth.screens.register.viewmodel

import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.screens.data.AuthState
import com.example.auth.screens.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()



    // Проверяем авторизацию при создании ViewModel
    init {
        checkAuthState()
    }



    private fun checkAuthState(){
        viewModelScope.launch {
            if (authRepository.isUserLoggedIn()) {
                _authState.value = AuthState.Loading
                authRepository.getCurrentUserProfile().fold(
                    onSuccess = { profile ->
                        _authState.value = AuthState.Authenticated(profile.role)
                    },
                    onFailure = {
                        // Если профиль не найден — разлогиниваем
                        authRepository.signOut()
                        _authState.value = AuthState.Idle
                    }
                )
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }



    suspend fun register(
        lastName: String,
        firstName: String,
        middleName: String,
        phone: String,
        email: String,
        address: String,
        role: String,
        orgCode: String,
        password: String,
        confirmPassword: String,
    ): Result<String> {
        _authState.value = AuthState.Loading

        return authRepository.registerUser(
            lastName,
            firstName, middleName = middleName,
            phone = phone,
            email= email,
            address = address,
            orgCode = orgCode,
            role = role,
            password = password,
            confirmPassword = confirmPassword
            ).fold(
            onSuccess = {
                _authState.value = AuthState.Authenticated(role)
                Result.success(role)
            },
            onFailure = { error ->
                _authState.value = AuthState.Error(error.message ?: "Unknown error")
                Result.failure(error)
            }
        )
    }

    suspend fun login(login: String, pass: String): Result<String> {
        _authState.value = AuthState.Loading

        return authRepository.signIn(login, pass).fold(
            onSuccess = { role ->
                _authState.value = AuthState.Authenticated(role)
                Result.success(role)
            },
            onFailure = { error ->
                _authState.value = AuthState.Error(error.message ?: "Unknown error")
                Result.failure(error)
            }
        )
    }

    fun logout() {
        authRepository.signOut()
        _authState.value = AuthState.Idle
    }



    /**
     * Сбросить состояние ошибки (например, при изменении текста в полях)
     */
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }
}