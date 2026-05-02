package com.example.manager.screens.orders.core

import androidx.compose.ui.graphics.Color
import com.example.manager.data.StatusConfig


fun getStatusConfig(status: String): StatusConfig {
    return when (status) {
        "PENDING" -> StatusConfig("Новый", Color(0xFFFEF3C7), Color(0xFFD97706))
        "IN_PROGRESS" -> StatusConfig("В работе", Color(0xFFDBEAFE), Color(0xFF2563EB))
        "COMPLETED" -> StatusConfig("Готов", Color(0xFFD1FAE5), Color(0xFF059669))
        "CANCELLED" -> StatusConfig("Отменён", Color(0xFFFEE2E2), Color(0xFFDC2626))
        else -> StatusConfig("Неизвестно", Color(0xFFF1F5F9), Color(0xFF64748B))
    }
}