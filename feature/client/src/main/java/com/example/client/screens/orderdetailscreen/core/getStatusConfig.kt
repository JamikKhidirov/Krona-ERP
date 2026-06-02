package com.example.client.screens.orderdetailscreen.core

import androidx.compose.ui.graphics.Color
import com.example.client.screens.orderdetailscreen.data.StatusConfig


fun getStatusConfig(status: String): StatusConfig {
    return when (status) {
        "PENDING" -> StatusConfig("Ожидает", Color(0xFFFEF3C7), Color(0xFFD97706))
        "ASSIGNED" -> StatusConfig("Назначен менеджеру", Color(0xFFEEF2FF), Color(0xFF6366F1))
        "IN_PROGRESS" -> StatusConfig("В работе", Color(0xFFDBEAFE), Color(0xFF2563EB))
        "READY" -> StatusConfig("Готов к выдаче", Color(0xFFD1FAE5), Color(0xFF059669))
        "DELIVERING" -> StatusConfig("Доставляется", Color(0xFFFEF3C7), Color(0xFFD97706))
        "COMPLETED" -> StatusConfig("Выполнен", Color(0xFFD1FAE5), Color(0xFF059669))
        "PAID" -> StatusConfig("Оплачен", Color(0xFFD1FAE5), Color(0xFF059669))
        "CANCELLED" -> StatusConfig("Отменён", Color(0xFFFEE2E2), Color(0xFFDC2626))
        "ACCEPTED" -> StatusConfig("Принят в работу", Color(0xFFD1FAE5), Color(0xFF059669))
        "MATERIALS" -> StatusConfig("Расход материалов", Color(0xFFFEF3C7), Color(0xFFD97706))
        "ASSEMBLY" -> StatusConfig("Сборка модулей", Color(0xFFDBEAFE), Color(0xFF2563EB))
        else -> StatusConfig("Неизвестно", Color(0xFFF1F5F9), Color(0xFF64748B))
    }
}