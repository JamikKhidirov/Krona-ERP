package com.example.auth.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth.screens.register.uicomponents.RoleDropdown
import com.example.auth.screens.register.viewmodel.AuthViewModel
import com.example.auth.uikit.KronaTextField
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (String) -> Unit, // Передаём роль после успеха
    viewModel: AuthViewModel = hiltViewModel()
) {
    // Состояния полей
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var orgCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Выберите роль") }

    // Состояния UI
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Валидация формы
    val isFormValid = remember(name, login, password, confirmPassword, selectedRole) {
        name.isNotBlank() &&
                login.isNotBlank() &&
                password.length >= 6 &&
                password == confirmPassword &&
                selectedRole != "Выберите роль"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F7)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(vertical = 60.dp),
            shape = RoundedCornerShape(30.dp),
            color = Color.White,
            shadowElevation = 25.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    text = "Крона",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF25326A)
                )
                Text(
                    text = "Регистрация",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Создайте аккаунт для доступа к системе управления мастерской.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                // Поля ввода
                KronaTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "ФИО",
                    placeholder = "Иванов Иван",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                KronaTextField(
                    value = login,
                    onValueChange = { login = it.filter { char -> !char.isWhitespace() } },
                    label = "Логин",
                    placeholder = "ivanov",
                    leadingIcon = Icons.Default.AccountCircle
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown ролей (только 2 роли: Клиент и Менеджер)
                RoleDropdown(
                    selectedRole = selectedRole,
                    onRoleSelected = { selectedRole = it }
                )

                // Код доступа только для Менеджера
                if (selectedRole == "Менеджер") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Для регистрации в роли Менеджера необходимо ввести код доступа.",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    KronaTextField(
                        value = orgCode,
                        onValueChange = { orgCode = it },
                        label = "Код доступа организации",
                        placeholder = "Введите код",
                        leadingIcon = Icons.Default.Lock,
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Пароль
                KronaTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Пароль",
                    placeholder = "Минимум 6 символов",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Подтверждение пароля
                KronaTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Подтверждение пароля",
                    placeholder = "Повторите пароль",
                    leadingIcon = Icons.Default.Refresh,
                    isError = confirmPassword.isNotBlank() && password != confirmPassword,
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                // Ошибка несовпадения паролей
                if (confirmPassword.isNotBlank() && password != confirmPassword) {
                    Text(
                        text = "Пароли не совпадают",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }

                // Сообщение об ошибке от сервера
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Кнопка регистрации
                Button(
                    onClick = {
                        errorMessage = null
                        isLoading = true

                        scope.launch {
                            val result = viewModel.register(
                                login = login.trim(),
                                pass = password,
                                fio = name.trim(),
                                role = selectedRole,
                                orgCode = orgCode.trim()
                            )

                            isLoading = false

                            result.onSuccess { role ->
                                Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess(role)
                            }.onFailure { error ->
                                errorMessage = error.message
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25326A)),
                    enabled = isFormValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Зарегистрироваться",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Переход к входу
                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Уже есть аккаунт? Войти",
                        color = Color(0xFF25326A),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}