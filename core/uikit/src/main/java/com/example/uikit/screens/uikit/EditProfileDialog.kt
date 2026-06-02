package com.example.uikit.screens.uikit



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uikit.Client


@Composable
fun EditProfileDialog(
    client: Client,
    onSave: (Client) -> Unit,
    onDismiss: () -> Unit
) {
    var firstName by remember { mutableStateOf(client.firstName) }
    var lastName by remember { mutableStateOf(client.lastName) }
    var middleName by remember { mutableStateOf(client.middleName) }
    var phone by remember { mutableStateOf(client.phone) }
    var email by remember { mutableStateOf(client.email) }
    var address by remember { mutableStateOf(client.address) }
    var emailError by remember { mutableStateOf<String?>(null) }

    val isEmailValid = email.contains("@")
    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && isEmailValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать профиль") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Фамилия") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Имя") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = middleName,
                    onValueChange = { middleName = it },
                    label = { Text("Отчество") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Телефон") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (it.isNotBlank() && !it.contains("@")) "Email должен содержать @" else null
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Адрес") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        client.copy(
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName,
                            phone = phone,
                            email = email,
                            address = address
                        )
                    )
                },
                enabled = isFormValid
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}