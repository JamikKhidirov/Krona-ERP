package com.example.auth.uikit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true)
fun AuthTextFild(
    value: String = "",
    modifier: Modifier = Modifier,
    textFildState: AuthTextFildState = AuthTextFildState.LOGIN,
    onValueTextChange: (String) -> Unit = {},
){
    val state by remember(textFildState){
        mutableStateOf(textFildState)
    }

    var passwordViz by remember {
        mutableStateOf(true)
    }


   when (state){
       AuthTextFildState.LOGIN -> {
           OutlinedTextField(
               value = value,
               onValueChange = onValueTextChange,
               modifier = modifier,
               singleLine = true,
               shape = RoundedCornerShape(12.dp),
               leadingIcon = {
                   Icon(
                       imageVector = Icons.Outlined.Person,
                       contentDescription = null,
                       tint = Color(0xFF767681),
                   )
               },
               placeholder = {
                   Text(
                       text = "Введите логин...",
                       color = Color(0xFF767681),
                       fontWeight = FontWeight(300)
                   )
               }

           )
       }

       AuthTextFildState.PASSWORD -> {
           OutlinedTextField(
               value = value,
               onValueChange = onValueTextChange,
               modifier = modifier,
               singleLine = true,
               shape = RoundedCornerShape(12.dp),
               visualTransformation = if (!passwordViz) PasswordVisualTransformation() else VisualTransformation.None,
               leadingIcon = {
                   Icon(
                       imageVector = Icons.Outlined.Lock,
                       contentDescription = null,
                       tint = Color(0xFF767681),
                   )
               },
               placeholder = {
                   Text(
                       text = "Введите пороль...",
                       fontSize = 16.sp,
                       color = Color(0xFF767681),
                       fontWeight = FontWeight(300)
                   )
               },
               trailingIcon = {
                   IconButton(
                       onClick = {
                           passwordViz = !passwordViz
                       }
                   ) {
                       Icon(
                           imageVector = if (passwordViz) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                           contentDescription = null,
                           tint = Color(0xFF767681),

                       )
                   }
               }

           )
       }
   }
}


enum class AuthTextFildState {

    LOGIN,
    PASSWORD

}