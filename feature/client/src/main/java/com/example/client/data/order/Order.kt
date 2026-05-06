package com.example.client.data.order

import android.os.Parcel
import android.os.Parcelable
import com.example.network.data.OrderStatus
import com.google.firebase.firestore.PropertyName



data class Order(
    @PropertyName("id") val id: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("productTypeId") val productTypeId: Int = 0,
    @PropertyName("productTypeName") val productTypeName: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("budget") val budget: String = "",
    @PropertyName("widthCm") val widthCm: String = "",
    @PropertyName("heightCm") val heightCm: String = "",
    @PropertyName("depthCm") val depthCm: String = "",
    @PropertyName("comment") val comment: String = "",
    @PropertyName("imageUrls") val imageUrls: List<String> = emptyList(),
    @PropertyName("status") val status: String = OrderStatus.PENDING.name,
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("updatedAt") val updatedAt: Long = System.currentTimeMillis()
)

