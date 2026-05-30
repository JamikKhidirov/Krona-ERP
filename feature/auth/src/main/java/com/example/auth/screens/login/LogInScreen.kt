package com.example.auth.screens.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import com.example.auth.screens.login.uicomponents.CardLogInScreen
import com.example.auth.screens.register.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


@Composable
fun LogInScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var logInText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardLogInScreen(
            onNewLogin = { login ->
                logInText = login.filter { !it.isWhitespace() }
                errorMessage = null
            },
            onNewPassword = { password ->
                passwordText = password
                errorMessage = null
            },
            onClickRegisterButton = {
                onNavigateToRegister()
            },
            onLogInClickButton = { login, password ->
                // Валидация
                if (login.isBlank()) {
                    errorMessage = "Введите логин"
                    return@CardLogInScreen
                }
                if (password.length < 6) {
                    errorMessage = "Пароль должен быть минимум 6 символов"
                    return@CardLogInScreen
                }

                isLoading = true
                errorMessage = null

                scope.launch {
                    viewModel.login(login.trim(), password).fold(
                        onSuccess = { role ->
                            isLoading = false
                            Toast.makeText(context, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(role)
                        },
                        onFailure = { error ->
                            isLoading = false
                            errorMessage = error.message ?: "Ошибка входа"
                        }
                    )
                }
            }
        )

        // Сообщение об ошибке под карточкой (если нужно вне карточки)
        AnimatedVisibility(visible = errorMessage != null) {
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 40.dp)
                )
            }
        }
    }
}