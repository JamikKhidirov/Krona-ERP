package com.example.manager.data




enum class OrderFilter(val label: String) {
    ALL("Все"),
    NEW("Новый"),

    MY_ORDERS("Мои заказы"),

    READY("Просмотренные"),

    IN_PROGRESS("В работе"),
    COMPLETED("Готов")


}