package com.example.manager.data

import com.google.firebase.firestore.PropertyName

data class Order(
    @PropertyName("id") val id: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("productTypeId") val productTypeId: Int = 1,
    @PropertyName("productTypeName") val productTypeName: String = "",
    @PropertyName("title") val title: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("budget") val budget: String = "",
    @PropertyName("widthCm") val widthCm: String = "",
    @PropertyName("heightCm") val heightCm: String = "",
    @PropertyName("depthCm") val depthCm: String = "",
    @PropertyName("imageUrls") val imageUrls: List<String> = emptyList(),
    @PropertyName("status") val status: String = "PENDING",
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("deadline") val deadline: String = "",
    @PropertyName("material") val material: String = "",
    @PropertyName("color") val color: String = "",
    @PropertyName("facade") val facade: String = "",
    @PropertyName("masterId") val masterId: String = "",
    @PropertyName("masterName") val masterName: String = "",
    @PropertyName("masterPhone") val masterPhone: String = "",
    @PropertyName("masterPhotoUrl") val masterPhotoUrl: String = "",
    @PropertyName("masterRating") val masterRating: Double = 0.0,
    @PropertyName("paidAmount") val paidAmount: String = "0",
    @PropertyName("comment") val comment: String = "",
    @PropertyName("clientName") val clientName: String = "",      // Новое: имя клиента
    @PropertyName("clientPhone") val clientPhone: String = "",    // Новое: телефон клиента
    @PropertyName("address") val address: String = ""             // Новое: адрес
) {
    fun getRemainingAmount(): String {
        val budgetVal = budget.replace(" ", "").replace("₽", "").toIntOrNull() ?: 0
        val paidVal = paidAmount.replace(" ", "").replace("₽", "").toIntOrNull() ?: 0
        return String.format("%,d", budgetVal - paidVal).replace(",", " ")
    }
}