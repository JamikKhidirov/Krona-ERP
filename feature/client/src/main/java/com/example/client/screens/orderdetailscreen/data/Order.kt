package com.example.client.screens.orderdetailscreen.data

import com.google.firebase.firestore.PropertyName
import java.security.Timestamp
import java.util.Date


data class Order(
    @PropertyName("id")
    val id: String = "",

    @PropertyName("userId")
    val userId: String = "",

    @PropertyName("productTypeId")
    val productTypeId: Int = 1,

    @PropertyName("productTypeName")
    val productTypeName: String = "",

    @PropertyName("title")
    val title: String = "", // "Кухонный гарнитур «Арктика»"

    @PropertyName("description")
    val description: String = "",

    @PropertyName("budget")
    val budget: String = "",

    @PropertyName("widthCm")
    val widthCm: String = "",

    @PropertyName("heightCm")
    val heightCm: String = "",

    @PropertyName("depthCm")
    val depthCm: String = "",

    @PropertyName("imageUrls")
    val imageUrls: List<String> = emptyList(),

    @PropertyName("status")
    val status: String = "PENDING", // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @PropertyName("statusHistory")
    val statusHistory: List<StatusUpdate> = emptyList(),

    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @PropertyName("deadline")
    val deadline: String = "", // "04.11.2023"

    @PropertyName("material")
    val material: String = "", // "МДФ Эмаль Мат"

    @PropertyName("color")
    val color: String = "", // "Белый (матовый)"

    @PropertyName("facade")
    val facade: String = "", // "Классический п-образный"

    @PropertyName("masterId")
    val masterId: String = "",

    @PropertyName("masterName")
    val masterName: String = "", // "Елена Волкова"

    @PropertyName("masterPhone")
    val masterPhone: String = "", // "+7 (900) 123-45-67"

    @PropertyName("masterPhotoUrl")
    val masterPhotoUrl: String = "",

    @PropertyName("masterRating")
    val masterRating: Double = 0.0,

    @PropertyName("documents")
    val documents: List<Document> = emptyList(),

    @PropertyName("paidAmount")
    val paidAmount: String = "0",

    @PropertyName("comment")
    val comment: String = "",

    @PropertyName("managerId")
    val managerId: String = "",

    @PropertyName("managerName")
    val managerName: String = "",

    @PropertyName("managerPhone")
    val managerPhone: String = ""
) {
    // Для Firestore — нужен пустой конструктор, уже есть default values

    fun getRemainingAmount(): String {
        val budgetVal = budget.replace(" ", "").replace("₽", "").toIntOrNull() ?: 0
        val paidVal = paidAmount.replace(" ", "").replace("₽", "").toIntOrNull() ?: 0
        val remaining = budgetVal - paidVal
        return String.format("%,d", remaining).replace(",", " ")
    }


    // Вспомогательное свойство для получения Date
    fun getCreatedAtDate(): Date = Date(createdAt)
}
