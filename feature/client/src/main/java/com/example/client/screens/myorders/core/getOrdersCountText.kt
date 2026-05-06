package com.example.client.screens.myorders.core




fun getOrdersCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "заказ"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "заказа"
        else -> "заказов"
    }
}