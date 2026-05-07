package com.example.manager.data




// История изменений
data class OrderHistoryItem(
    val id: String = "",
    val status: String = "",
    val managerId: String = "",
    val managerName: String = "",
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)