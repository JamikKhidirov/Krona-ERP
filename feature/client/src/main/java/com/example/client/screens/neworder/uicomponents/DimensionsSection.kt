package com.example.client.screens.neworder.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// DimensionsSection.kt
@Composable
fun DimensionsSection(
    width: String,
    height: String,
    depth: String,
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onDepthChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Размеры (см)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ширина
            OutlinedTextField(
                value = width,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                        onWidthChange(it)
                    }
                },
                label = { Text("Ширина") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                suffix = { Text("см") }
            )

            // Высота
            OutlinedTextField(
                value = height,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                        onHeightChange(it)
                    }
                },
                label = { Text("Высота") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                suffix = { Text("см") }
            )

            // Глубина
            OutlinedTextField(
                value = depth,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                        onDepthChange(it)
                    }
                },
                label = { Text("Глубина") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                suffix = { Text("см") }
            )
        }
    }
}