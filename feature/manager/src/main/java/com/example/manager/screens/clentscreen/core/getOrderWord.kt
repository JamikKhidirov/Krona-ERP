package com.example.manager.screens.clentscreen.core



fun getOrderWord(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "активный заказ"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "активных заказа"
        else -> "активных заказов"
    }
}
