package com.example.manager.data




enum class OrderFilter(val label: String) {
    ALL("Все"),
    NEW("Новый"),
    IN_PROGRESS("В работе"),
    COMPLETED("Готов")
}