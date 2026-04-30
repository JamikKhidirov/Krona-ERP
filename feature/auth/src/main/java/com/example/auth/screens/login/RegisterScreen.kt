package com.example.auth.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.uicomponents.KronaTextField


@Composable
@Preview(showBackground = true)
fun RegisterScreen(){



    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var orgCode by remember { mutableStateOf("") }

    var role by remember { mutableStateOf("Выберите роль") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF5F5F7)
            ),
        contentAlignment = Alignment.Center
    ){

        // Белая карточка
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Чтобы влезло на маленькие экраны
                .padding(
                    vertical = 40.dp,
                    horizontal = 15.dp
                ),
            shape = RoundedCornerShape(16.dp),
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
                    value = name, onValueChange =  { name = it },
                    label = "ФИО",
                    placeholder = "Иванов Иван Иванович",
                    leadingIcon = Icons.Default.Person)

                KronaTextField(value = login,
                    onValueChange = { login = it },
                    label = "Логин",
                    placeholder = "Введите логин",
                    leadingIcon = Icons.Default.AccountCircle)

                // Поле выбора роли (упрощено)
                KronaTextField(
                    value = role,
                    onValueChange = {newRole ->
                        role = newRole
                    }
                    , label = "Роль в системе",
                    placeholder = "Выберите роль",
                    leadingIcon = Icons.Default.List
                )

                // Подсказка про Менеджера
                Text(
                    text = "Для регистрации в роли Менеджера необходимо ввести код доступа администратора.",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                KronaTextField(
                    value =  orgCode,
                    onValueChange = { orgCode = it },
                    label =  "Код доступа организации",
                    placeholder = "Введите код доступа",
                    leadingIcon =  Icons.Default.Lock
                )
                KronaTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Пароль",
                    placeholder = "••••••••",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = PasswordVisualTransformation()
                )
                KronaTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Подтверждение пароля",
                    placeholder = "••••••••",
                    leadingIcon = Icons.Default.Refresh,
                    visualTransformation = PasswordVisualTransformation()
                )

                // Кнопка регистрации (используем ваш стиль из предыдущего вопроса)
                Button(
                    onClick = { /* Регистрация */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25326A))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Зарегистрироваться", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }

                // Футер
                Row(modifier = Modifier.padding(top = 24.dp)) {
                    Text("Уже есть аккаунт? ", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "Войти",
                        fontSize = 14.sp,
                        color = Color(0xFF25326A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* На логин */ }
                    )
                }
            }
        }
    }

}

