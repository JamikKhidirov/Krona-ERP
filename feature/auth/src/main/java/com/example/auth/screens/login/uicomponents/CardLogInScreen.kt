package com.example.auth.screens.login.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.R
import com.example.auth.uikit.AuthButton
import com.example.auth.uikit.AuthButtonState
import com.example.auth.uikit.AuthTextFild
import com.example.auth.uikit.AuthTextFildState
import com.example.uikit.AuthText


@Composable
@Preview(showBackground = true)
fun CardLogInScreen(
    modifier: Modifier = Modifier,
    onNewLogin: (String) -> Unit = {},
    onNewPassword: (String) -> Unit = {},
    onLogInClickButton: (login: String, password: String) -> Unit = { _, _ ->},
    onClickRegisterButton: () -> Unit = {}
){
    var passwordText by remember {
        mutableStateOf("")
    }
    var loginText by remember {
        mutableStateOf("")
    }

    Card(
        modifier = modifier
            .width(342.dp)
            .height(646.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F9FB)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Icon(
                painter = painterResource(R.drawable.work_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(
                    top = 33.dp
                )
            )
            Text(
                text = "Крона - Учёт заказов",
                color = Color(0xFF25326A),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )

            Text(
                text = "Войдите в систему для управления\nмастерской",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF454650),
                modifier = Modifier
                    .padding(top = 5.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column() {
                    AuthText(
                        text = "Логин",
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )

                    AuthTextFild(
                        textFildState = AuthTextFildState.LOGIN,
                        modifier = Modifier
                            .padding(
                                top = 4.dp
                            )
                            .height(50.dp),
                        onValueTextChange = { newLogin ->
                            loginText = newLogin
                            onNewLogin(newLogin)
                        },
                        value = loginText
                    )

                    AuthText(
                        text = "Пороль",
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = 8.dp
                            )
                    )

                    AuthTextFild(
                        textFildState = AuthTextFildState.PASSWORD,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(50.dp),
                        onValueTextChange = { newPassword ->
                            passwordText = newPassword
                            onNewPassword(newPassword)
                        },
                        value = passwordText
                    )


                    AuthButton(
                        buttonState = AuthButtonState.LOGIN,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 10.dp),
                        onClickButton = {
                            onLogInClickButton(
                                loginText,
                                passwordText
                            )
                        }
                    )

                    AuthButton(
                        buttonState = AuthButtonState.REGISTER,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onClickButton = {
                            onClickRegisterButton()
                        }
                    )



                }
            }
        }
    }
}