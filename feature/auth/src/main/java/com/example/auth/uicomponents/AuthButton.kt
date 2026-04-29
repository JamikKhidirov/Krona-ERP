package com.example.auth.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true)
fun AuthButton(
    modifier: Modifier = Modifier,
    buttonState: AuthButtonState = AuthButtonState.REGISTER,
    onClickButton: () -> Unit = {}
){
    val state by remember(buttonState) {
        mutableStateOf(buttonState)
    }

    when (state) {
        AuthButtonState.LOGIN -> {
            Button(
                modifier = modifier,
                onClick = onClickButton,
                shape = RoundedCornerShape(
                    12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25326A)
                )
            ) {
                Text(
                    text = "Войти",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        vertical = 10.dp,
                        horizontal = 112.dp)
                )
            }
        }
        AuthButtonState.REGISTER -> {
            OutlinedButton(
                modifier = modifier,
                onClick = onClickButton,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFF25326A),
                )
            ) {
                Text(
                    text = "Регистрация",
                    modifier = Modifier.padding(
                        vertical = 10.dp,
                        horizontal = 112.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF25326A)
                )
            }
        }
    }
}


enum class AuthButtonState {
    LOGIN,
    REGISTER
}