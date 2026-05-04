package com.example.network.data



enum class OrderStatus {
    PENDING,       // Ожидает мастера
    IN_PROGRESS,   // В работе
    COMPLETED,     // Выполнен
    CANCELLED      // Отменён
}
