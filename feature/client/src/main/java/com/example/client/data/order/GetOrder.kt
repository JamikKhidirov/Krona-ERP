package com.example.client.data.order



data class GetOrder(
    val id: String = "",
    val userId: String = "",
    val productTypeId: Int = 0,
    val productTypeName: String = "",
    val description: String = "",
    val budget: String = "",
    val widthCm: String = "",
    val heightCm: String = "",
    val depthCm: String = "",
    val comment: String = "",
    val imageUrls: List<String> = emptyList(),
    val status: String = "PENDING",  // ← Просто строка!
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
