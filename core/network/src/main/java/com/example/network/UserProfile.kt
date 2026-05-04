package com.example.network




data class UserProfile(
    val uid: String = "",
    val fio: String = "",
    val login: String = "",
    val email: String = "",
    val role: String = "Менеджер", // "Manager" или "Client"
    val createdAt: Long = 0L
)



data class ClientRegistrationData(
    val lastName: String = "",      // Фамилия
    val firstName: String = "",     // Имя
    val middleName: String = "",    // Отчество
    val phone: String = "",         // Телефон
    val email: String = "",         // Email (логин)
    val address: String = "",       // Адрес
    val password: String = "",      // Пароль
    val confirmPassword: String = "", // Подтверждение пароля
    val role: String = "Клиент",    // Роль
    val orgCode: String = ""        // Код доступа (для менеджера)
)
