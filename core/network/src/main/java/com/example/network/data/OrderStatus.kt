package com.example.network.data



enum class OrderStatus {
    PENDING,      // Ожидает — новый заказ, не назначен
    ASSIGNED,     // Назначен — есть менеджер ← НОВЫЙ
    IN_PROGRESS,  // В работе
    READY,        // Готов к доставке ← НОВЫЙ
    DELIVERING,   // Доставляется ← НОВЫЙ
    COMPLETED,    // Выполнен
    CANCELLED     // Отменён

}
