package com.example.manager.data

import com.google.firebase.firestore.PropertyName
import kotlin.math.absoluteValue


data class Client(
    @PropertyName("id") val id: String = "",
    @PropertyName("uid") val uid: String = "",
    @PropertyName("firstName") val firstName: String = "",
    @PropertyName("lastName") val lastName: String = "",
    @PropertyName("middleName") val middleName: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("address") val address: String = "",
    @PropertyName("role") val role: String = "Клиент",
    @PropertyName("avatarUrl") val avatarUrl: String = "",
    @PropertyName("orderCount") val orderCount: Int = 0,
    @PropertyName("activeOrderCount") val activeOrderCount: Int = 0,
    @PropertyName("furnitureTypes") val furnitureTypes: List<String> = emptyList(),
    @PropertyName("totalSpent") val totalSpent: String = "0",
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("lastOrderDate") val lastOrderDate: String = "",
    @PropertyName("notes") val notes: String = ""
) {
    fun getFullName(): String = buildString {
        append(lastName)
        if (lastName.isNotEmpty() && firstName.isNotEmpty()) append(" ")
        append(firstName)
        if (middleName.isNotEmpty()) append(" $middleName")
    }.ifEmpty { "Неизвестный клиент" }

    fun getInitials(): String {
        val first = firstName.take(1).uppercase()
        val last = lastName.take(1).uppercase()
        return "$last$first".ifEmpty { "?" }
    }

    fun getAvatarColor(): Long {
        // Генерируем стабильный цвет по ID
        val colors = listOf(
            0xFF6366F1, // Indigo
            0xFFEC4899, // Pink
            0xFFF59E0B, // Amber
            0xFF10B981, // Emerald
            0xFF8B5CF6, // Violet
            0xFFEF4444, // Red
            0xFF06B6D4, // Cyan
            0xFF84CC16  // Lime
        )
        val index = id.hashCode().absoluteValue % colors.size
        return colors[index]
    }
}