package com.example.client.screens.orderdetailscreen.uikit


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.client.screens.orderdetailscreen.data.Order


@Composable
fun FinanceSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Финансы",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinanceRow(
                label = "Стоимость заказа",
                value = "${order.budget} ₽",
                isTotal = false
            )

            val paid = order.paidAmount.ifEmpty { "0" }
            FinanceRow(
                label = "Оплачено",
                value = "$paid ₽",
                isTotal = false,
                valueColor = MaterialTheme.colorScheme.tertiary
            )

            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            FinanceRow(
                label = "Осталось",
                value = "${order.getRemainingAmount()} ₽",
                isTotal = true,
                valueColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}
