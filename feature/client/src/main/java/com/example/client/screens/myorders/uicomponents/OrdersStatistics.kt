package com.example.client.screens.myorders.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.network.data.OrderStatus



@Composable
fun OrdersStatistics(orders: List<com.example.client.data.order.Order>) {
    val activeCount = orders.count { it.status == OrderStatus.IN_PROGRESS.name }
    val pendingCount = orders.count { it.status == OrderStatus.PENDING.name }
    val completedCount = orders.count { it.status == OrderStatus.COMPLETED.name }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                count = orders.size,
                label = "Всего",
                color = MaterialTheme.colorScheme.primary,
            )
            StatItem(
                count = activeCount,
                label = "В работе",
                color = Color(0xFF3B82F6)
            )
            StatItem(
                count = pendingCount,
                label = "Ожидают",
                color = Color(0xFFF59E0B)
            )
            StatItem(
                count = completedCount,
                label = "Готово",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}