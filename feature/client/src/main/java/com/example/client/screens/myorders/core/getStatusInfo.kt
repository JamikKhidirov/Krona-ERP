package com.example.client.screens.myorders.core

import androidx.compose.ui.graphics.Color
import com.example.client.screens.myorders.core.data.StatusInfo
import com.example.network.data.OrderStatus


fun getStatusInfo(name: String): StatusInfo {
    return when (name) {
        OrderStatus.PENDING.name -> StatusInfo(
            label = "Ожидает",
            color = Color(0xFFF59E0B),
            backgroundColor = Color(0xFFFEF3C7)
        )
        OrderStatus.ASSIGNED.name -> StatusInfo(
            label = "Назначен менеджеру",
            color = Color(0xFF6366F1),
            backgroundColor = Color(0xFFEEF2FF)
        )
        OrderStatus.IN_PROGRESS.name -> StatusInfo(
            label = "В работе",
            color = Color(0xFF3B82F6),
            backgroundColor = Color(0xFFDBEAFE)
        )
        OrderStatus.READY.name -> StatusInfo(
            label = "Готов к выдаче",
            color = Color(0xFF10B981),
            backgroundColor = Color(0xFFD1FAE5)
        )
        OrderStatus.DELIVERING.name -> StatusInfo(
            label = "Доставляется",
            color = Color(0xFF8B5CF6),
            backgroundColor = Color(0xFFEDE9FE)
        )
        OrderStatus.COMPLETED.name-> StatusInfo(
            label = "Выполнен",
            color = Color(0xFF10B981),
            backgroundColor = Color(0xFFD1FAE5)
        )
        OrderStatus.CANCELLED.name -> StatusInfo(
            label = "Отменён",
            color = Color(0xFFEF4444),
            backgroundColor = Color(0xFFFEE2E2)
        )
        else -> StatusInfo(
            label = name,
            color = Color(0xFF94A3B8),
            backgroundColor = Color(0xFFF1F5F9)
        )
    }
}