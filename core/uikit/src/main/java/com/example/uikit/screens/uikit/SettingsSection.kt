package com.example.uikit.screens.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSection(
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onForgotPassword: () -> Unit = {},
    onEmailVerification: () -> Unit = {},
    isEmailVerified: Boolean = false,
    email: String = ""
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            SettingsItem(Icons.Default.Edit, "Редактировать профиль", onEditProfile)

            Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 20.dp))

            SettingsItem(Icons.Default.Lock, "Изменить пароль", onChangePassword)

            Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 20.dp))

            SettingsItem(Icons.Default.Refresh, "Забыли пароль?", onForgotPassword)

            if (email.isNotBlank()) {
                Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onEmailVerification)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isEmailVerified) Icons.Default.VerifiedUser else Icons.Default.Email,
                        contentDescription = null,
                        tint = if (isEmailVerified) Color(0xFF10B981) else Color(0xFF6366F1),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isEmailVerified) "Email подтверждён" else "Подтвердить email",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    if (!isEmailVerified) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}
