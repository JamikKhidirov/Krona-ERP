package com.example.client.screens.chats

data class ChatThread(
    val orderId: String = "",
    val orderTitle: String = "",
    val clientName: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0
) {
    val formattedTime: String
        get() {
            if (lastMessageTime == 0L) return ""
            val sdf = java.text.SimpleDateFormat("dd.MM HH:mm", java.util.Locale.getDefault())
            return sdf.format(java.util.Date(lastMessageTime))
        }
}
