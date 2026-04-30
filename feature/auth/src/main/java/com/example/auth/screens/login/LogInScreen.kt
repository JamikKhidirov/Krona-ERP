package com.example.auth.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.auth.screens.login.uicomponents.CardLogInScreen


@Preview(showBackground = true)
@Composable
fun LogInScreen(){

    var logInText by remember {
        mutableStateOf("")
    }

    var passwordText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardLogInScreen(
            onNewLogin = {login ->
                logInText = login
            },
            onNewPassword = { password ->
                passwordText = password
            },
            onClickRegisterButton = {
                //Переход на экран регистрации
            },
            onLogInClickButton = {login, password ->
                //Логин пользователя
            }
        )
    }
}

