package com.example.network




data class UserProfile(
    val uid: String = "",
    val fio: String = "",
    val login: String = "",
    val role: String = "Client", // "Manager" или "Client"
    val organizationCode: String = ""
)
