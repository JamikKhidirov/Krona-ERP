package com.example.manager.screens.orders.core

import com.example.network.data.OrderStatus


fun getStatusLabel(status: OrderStatus): String {
    return when (status) {
        OrderStatus.PENDING -> "Новый"
        OrderStatus.ASSIGNED -> "Назначен"
        OrderStatus.IN_PROGRESS -> "В работе"
        OrderStatus.READY -> "Готов"
        OrderStatus.DELIVERING -> "Доставляется"
        OrderStatus.COMPLETED -> "Выполнен"
        OrderStatus.CANCELLED -> "Отменён"
    }
}