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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.KeyboardType
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
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // === СОСТОЯНИЯ ПОЛЕЙ (как в Client) ===
    var lastName by remember { mutableStateOf("") }      // Фамилия
    var firstName by remember { mutableStateOf("") }     // Имя
    var middleName by remember { mutableStateOf("") }    // Отчество
    var phone by remember { mutableStateOf("") }         // Телефон
    var email by remember { mutableStateOf("") }         // Email = логин
    var address by remember { mutableStateOf("") }       // Адрес
    var password by remember { mutableStateOf("") }      // Пароль
    var confirmPassword by remember { mutableStateOf("") } // Подтверждение
    var selectedRole by remember { mutableStateOf("Клиент") } // Роль
    var orgCode by remember { mutableStateOf("") }

    // Состояния UI
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // ФИО для отображения (как в Client.getFullName())
    val fullName = remember(lastName, firstName, middleName) {
        buildString {
            append(lastName)
            if (lastName.isNotEmpty() && firstName.isNotEmpty()) append(" ")
            append(firstName)
            if (middleName.isNotEmpty()) append(" $middleName")
        }
    }

    // Валидация формы
    val isFormValid = remember(
        lastName, firstName, phone, email, password, confirmPassword, selectedRole
    ) {
        lastName.isNotBlank() &&
                firstName.isNotBlank() &&
                phone.isNotBlank() &&
                email.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6 &&
                password == confirmPassword &&
                selectedRole in listOf("Клиент", "Менеджер")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(vertical = 40.dp),
            shape = RoundedCornerShape(30.dp),
                        color = MaterialTheme.colorScheme.surface,
            shadowElevation = 25.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // === ЗАГОЛОВОК ===
                Text(
                    text = "Крона",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
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
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )

                // === ФАМИЛИЯ ===
                KronaTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        errorMessage = null
                    },
                    label = "Фамилия",
                    placeholder = "Иванов",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(10.dp))

                // === ИМЯ ===
                KronaTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        errorMessage = null
                    },
                    label = "Имя",
                    placeholder = "Иван",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(10.dp))

                // === ОТЧЕСТВО ===
                KronaTextField(
                    value = middleName,
                    onValueChange = {
                        middleName = it
                        errorMessage = null
                    },
                    label = "Отчество (необязательно)",
                    placeholder = "Иванович",
                    leadingIcon = Icons.Default.Person,

                )

                Spacer(modifier = Modifier.height(10.dp))

                // === ТЕЛЕФОН ===
                KronaTextField(
                    value = phone,
                    onValueChange = {
                        phone = it.filter { c -> c.isDigit() || c == '+' || c == '-' || c == ' ' }
                        errorMessage = null
                    },
                    label = "Телефон",
                    placeholder = "+7 (900) 123-45-67",
                    leadingIcon = Icons.Default.Phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // === EMAIL (логин) ===
                KronaTextField(
                    value = email,
                    onValueChange = {
                        email = it.filter { c -> !c.isWhitespace() }
                        errorMessage = null
                    },
                    label = "Email (логин)",
                    placeholder = "ivanov@mail.ru",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // === АДРЕС ===
                KronaTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        errorMessage = null
                    },
                    label = "Адрес (необязательно)",
                    placeholder = "г. Москва, ул. Ленина, д. 1",
                    leadingIcon = Icons.Default.LocationOn
                )

                Spacer(modifier = Modifier.height(10.dp))

                // === РОЛЬ ===
                RoleDropdown(
                    selectedRole = selectedRole,
                    onRoleSelected = { selectedRole = it }
                )

                // === КОД ДОСТУПА (только для Менеджера) ===
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

                Spacer(modifier = Modifier.height(10.dp))

                // === ПАРОЛЬ ===
                KronaTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
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

                Spacer(modifier = Modifier.height(10.dp))

                // === ПОДТВЕРЖДЕНИЕ ПАРОЛЯ ===
                KronaTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
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
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }

                // === ОШИБКА ОТ СЕРВЕРА ===
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // === КНОПКА РЕГИСТРАЦИИ ===
                Button(
                    onClick = {
                        errorMessage = null
                        isLoading = true

                        scope.launch {
                            // Собираем ФИО как в Client
                            val fio = buildString {
                                append(lastName)
                                append(" ")
                                append(firstName)
                                if (middleName.isNotBlank()) {
                                    append(" ")
                                    append(middleName)
                                }
                            }

                            val result = viewModel.register(
                                lastName = lastName.trim(),  // Email = логин
                                firstName = firstName,
                                middleName = middleName,
                                phone = phone,
                                email = email,
                                address = address,
                                role = selectedRole,
                                orgCode = if (selectedRole == "Менеджер") orgCode.trim() else "",
                                password = password,
                                confirmPassword = confirmPassword,
                            )

                            isLoading = false

                            result.onSuccess { role ->
                                Toast.makeText(
                                    context,
                                    "Добро пожаловать, $firstName!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = isFormValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.surface,
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

                Spacer(modifier = Modifier.height(12.dp))

                // === ПЕРЕХОД К ВХОДУ ===
                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Уже есть аккаунт? Войти",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}