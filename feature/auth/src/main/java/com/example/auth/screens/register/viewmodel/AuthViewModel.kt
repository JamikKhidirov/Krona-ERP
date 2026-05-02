package com.example.auth.screens.register.viewmodel

import androidx.lifecycle.ViewModel
import com.example.auth.screens.data.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    suspend fun register(
        login: String,
        pass: String,
        fio: String,
        role: String,
        orgCode: String
    ): Result<String> {
        _authState.value = AuthState.Loading

        return authRepository.registerUser(login, pass, fio, role, orgCode).fold(
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
}