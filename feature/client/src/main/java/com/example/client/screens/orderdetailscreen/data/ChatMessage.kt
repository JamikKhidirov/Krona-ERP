package com.example.client.screens.orderdetailscreen.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}
