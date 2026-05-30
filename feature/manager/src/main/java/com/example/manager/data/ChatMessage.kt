package com.example.manager.data

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedTime: String
        get() {
            val sdf = java.text.SimpleDateFormat("dd.MM HH:mm", java.util.Locale.getDefault())
            return sdf.format(java.util.Date(timestamp))
        }
}
