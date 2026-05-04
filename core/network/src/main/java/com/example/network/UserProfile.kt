package com.example.network




data class UserProfile(
    val uid: String = "",
    val fio: String = "",
    val login: String = "",
    val email: String = "",
    val role: String = "Менеджер", // "Manager" или "Client"
    val createdAt: Long = 0L
)
