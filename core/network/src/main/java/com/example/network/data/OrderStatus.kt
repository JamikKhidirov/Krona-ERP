package com.example.network.data



enum class OrderStatus {
    PENDING,       // Ожидает мастера
    IN_PROGRESS,   // В работе
    COMPLETED,     // Выполнен
    DELIVERED,    // Доставлен ← добавь если нет
    CANCELLED     // Отменён

}
