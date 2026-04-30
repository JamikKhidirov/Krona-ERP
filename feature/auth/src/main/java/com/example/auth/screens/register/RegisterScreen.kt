package com.example.auth.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.screens.register.uicomponents.ButtonAuthNavLogIn
import com.example.auth.screens.register.uicomponents.ManualRoleDropdown
import com.example.auth.uicomponents.KronaTextField

@Composable
@Preview(showBackground = true)
fun RegisterScreen() {
    // 1. Все состояния экрана храним здесь
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var orgCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Начальное значение для роли
    var selectedRole by remember { mutableStateOf("Выберите роль") }

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
                Text(text = "Крона", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25326A))
                Text(text = "Регистрация", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
                Text(
                    text = "Создайте аккаунт для доступа к системе управления мастерской.",
                    fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                // Поля ФИО и Логин
                KronaTextField(value = name, onValueChange = { name = it }, label = "ФИО", placeholder = "Иванов Иван", leadingIcon = Icons.Default.Person)
                KronaTextField(value = login, onValueChange = { login = it }, label = "Логин", placeholder = "Введите логин", leadingIcon = Icons.Default.AccountCircle)

                // 2. ВСТАВЛЯЕМ ТВОЙ DROPDOWN
                // Мы передаем текущую роль и функцию, которая её изменит
                ManualRoleDropdown(
                    selectedRole = selectedRole,
                    onRoleSelected = { newValue -> selectedRole = newValue }
                )

                // 3. ЛОГИКА ПОКАЗА КОДА (только если выбран Менеджер)
                if (selectedRole == "Менеджер") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Для регистрации в роли Менеджера необходимо ввести код доступа.",
                        fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start
                    )
                    KronaTextField(value = orgCode, onValueChange = { orgCode = it }, label = "Код доступа организации", placeholder = "Введите код", leadingIcon = Icons.Default.Lock)
                }

                // Пароли
                KronaTextField(value = password, onValueChange = { password = it }, label = "Пароль", placeholder = "••••••••", leadingIcon = Icons.Default.Lock, visualTransformation = PasswordVisualTransformation())
                KronaTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Подтверждение пароля", placeholder = "••••••••", leadingIcon = Icons.Default.Refresh, visualTransformation = PasswordVisualTransformation())

                // Кнопка
                Button(
                    onClick = { /* Логика регистрации */ },
                    modifier = Modifier.fillMaxWidth().height(64.dp).padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25326A))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Зарегистрироваться", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                    }
                }

                // Футер
                ButtonAuthNavLogIn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    onClickButton = {

                    }
                )
            }
        }
    }
}