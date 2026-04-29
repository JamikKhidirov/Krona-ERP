package com.example.auth.uicomponents

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview



@Composable
@Preview(showBackground = true)
fun AuthTextFild(
    value: String = "",
    onValueTextChange: (String) -> Unit = {},
    textFildState: AuthTextFildState = AuthTextFildState.LOGIN
){
    val state by remember(textFildState){
        mutableStateOf(textFildState)
    }


    OutlinedTextField(
        value = value,
        onValueChange = onValueTextChange,
        singleLine = true

    )
}


enum class AuthTextFildState {

    LOGIN,
    PASSWORD

}