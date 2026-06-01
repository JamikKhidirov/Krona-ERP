package com.example.manager.screens.orders.core

import androidx.compose.ui.graphics.Color
import com.example.manager.data.StatusConfig
import com.example.network.data.OrderStatus


fun getStatusConfig(status: String): StatusConfig {
    return when (status) {
        OrderStatus.PENDING.name -> StatusConfig("Новый", Color(0xFFF59E0B), Color(0xFFFEF3C7))
        OrderStatus.ASSIGNED.name -> StatusConfig("Назначен", Color(0xFF6366F1), Color(0xFFEEF2FF))
        OrderStatus.IN_PROGRESS.name -> StatusConfig("В работе", Color(0xFF3B82F6), Color(0xFFDBEAFE))
        OrderStatus.READY.name -> StatusConfig("Готов", Color(0xFF8B5CF6), Color(0xFFEDE9FE))
        OrderStatus.DELIVERING.name -> StatusConfig("Доставка", Color(0xFF06B6D4), Color(0xFFCFFAFE))
        OrderStatus.COMPLETED.name -> StatusConfig("Выполнен", Color(0xFF10B981), Color(0xFFD1FAE5))
        OrderStatus.PAID.name -> StatusConfig("Оплачен", Color(0xFF059669), Color(0xFFD1FAE5))
        OrderStatus.CANCELLED.name -> StatusConfig("Отменён", Color(0xFFEF4444), Color(0xFFFEE2E2))
        else -> StatusConfig(status, Color(0xFF94A3B8), Color(0xFFF1F5F9))
    }
}