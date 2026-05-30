package com.example.uikit.screens.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordDialog(
    currentEmail: String,
    onChangePassword: (currentPassword: String, newPassword: String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }

    val isFormValid = currentPassword.length >= 6 &&
            newPassword.length >= 6 &&
            newPassword == confirmPassword &&
            newPassword != currentPassword

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Изменить пароль",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Аккаунт: $currentEmail",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Текущий пароль") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = if (currentVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { currentVisible = !currentVisible }) {
                            Icon(
                                if (currentVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                null
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Новый пароль") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = if (newVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { newVisible = !newVisible }) {
                            Icon(
                                if (newVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                null
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Подтвердите новый пароль") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPassword.isNotEmpty() && newPassword != confirmPassword
                )

                if (confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                    Text(
                        "Пароли не совпадают",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (newPassword.isNotEmpty() && currentPassword == newPassword) {
                    Text(
                        "Новый пароль должен отличаться от текущего",
                        color = Color(0xFFF59E0B),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onChangePassword(currentPassword, newPassword) },
                enabled = isFormValid,
                colors = ButtonDefaults.textButtonColors(
                    disabledContentColor = Color(0xFF94A3B8)
                )
            ) {
                Text("Изменить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
