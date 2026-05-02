package com.example.network





data class UserProfile(
    val uid: String = "",
    val fio: String = "",
    val login: String = "",      // Оригинальный логин (без @krona.app)
    val email: String = "",      // Полный email для Firebase Auth
    val role: String = "Client", // "Manager" или "Client"
    val createdAt: Long = 0L
)