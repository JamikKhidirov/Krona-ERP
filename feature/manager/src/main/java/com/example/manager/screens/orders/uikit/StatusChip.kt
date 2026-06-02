package com.example.manager.screens.orders.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val config = getStatusConfig(status)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = config.backgroundColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Индикатор-точка
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(config.dotColor)
            )

            Text(
                text = config.label,
                style = MaterialTheme.typography.labelSmall,
                color = config.textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Конфигурация статусов
@Composable
private fun getStatusConfig(status: String): StatusConfig {
    return when (status.uppercase()) {
        "PENDING", "NEW", "ОЖИДАЕТ" -> StatusConfig(
            label = "Ожидает",
            textColor = Color(0xFFB45309),
            backgroundColor = Color(0xFFFEF3C7),
            dotColor = Color(0xFFF59E0B)
        )
        "ASSIGNED", "НАЗНАЧЕН" -> StatusConfig(
            label = "Назначен",
            textColor = Color(0xFF4338CA),
            backgroundColor = Color(0xFFEEF2FF),
            dotColor = Color(0xFF6366F1)
        )
        "IN_PROGRESS", "В_РАБОТЕ", "В РАБОТЕ" -> StatusConfig(
            label = "В работе",
            textColor = Color(0xFF1D4ED8),
            backgroundColor = Color(0xFFDBEAFE),
            dotColor = Color(0xFF3B82F6)
        )
        "READY", "ГОТОВ" -> StatusConfig(
            label = "Готов",
            textColor = Color(0xFF7C3AED),
            backgroundColor = Color(0xFFEDE9FE),
            dotColor = Color(0xFF8B5CF6)
        )
        "DELIVERING", "ДОСТАВКА", "ДОСТАВЛЯЕТСЯ" -> StatusConfig(
            label = "Доставка",
            textColor = Color(0xFF0E7490),
            backgroundColor = Color(0xFFCFFAFE),
            dotColor = Color(0xFF06B6D4)
        )
        "COMPLETED", "DONE", "ВЫПОЛНЕН", "ЗАВЕРШЁН" -> StatusConfig(
            label = "Выполнен",
            textColor = Color(0xFF047857),
            backgroundColor = Color(0xFFD1FAE5),
            dotColor = Color(0xFF10B981)
        )
        "PAID", "ОПЛАЧЕН" -> StatusConfig(
            label = "Оплачен",
            textColor = Color(0xFF065F46),
            backgroundColor = Color(0xFFD1FAE5),
            dotColor = Color(0xFF059669)
        )
        "CANCELLED", "CANCELED", "ОТМЕНЁН", "ОТМЕНЕН" -> StatusConfig(
            label = "Отменён",
            textColor = Color(0xFFB91C1C),
            backgroundColor = Color(0xFFFEE2E2),
            dotColor = Color(0xFFEF4444)
        )
        "ON_HOLD", "ПРИОСТАНОВЛЕН" -> StatusConfig(
            label = "Приостановлен",
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            dotColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        else -> StatusConfig(
            label = status,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            dotColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class StatusConfig(
    val label: String,
    val textColor: Color,
    val backgroundColor: Color,
    val dotColor: Color
)