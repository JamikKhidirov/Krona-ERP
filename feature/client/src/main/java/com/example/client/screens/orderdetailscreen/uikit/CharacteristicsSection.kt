package com.example.client.screens.orderdetailscreen.uikit


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.client.screens.orderdetailscreen.data.Order



@Composable
fun CharacteristicsSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Характеристики",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val characteristics = listOfNotNull(
                "Материал" to order.material,
                "Цвет" to order.color,
                "Фасад" to order.facade,
                "Размеры" to buildString {
                    if (order.widthCm.isNotEmpty()) append("Ш: ${order.widthCm} см")
                    if (order.heightCm.isNotEmpty()) {
                        if (isNotEmpty()) append(", ")
                        append("В: ${order.heightCm} см")
                    }
                    if (order.depthCm.isNotEmpty()) {
                        if (isNotEmpty()) append(", ")
                        append("Г: ${order.depthCm} см")
                    }
                }.takeIf { it.isNotEmpty() }
            )

            characteristics.forEach { (label, value) ->
                CharacteristicRow(label = label, value = value)
            }
        }
    }
}