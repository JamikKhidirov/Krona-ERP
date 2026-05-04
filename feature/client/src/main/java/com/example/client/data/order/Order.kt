package com.example.client.data.order

import android.os.Parcel
import android.os.Parcelable
import com.example.network.data.OrderStatus


data class Order(
    val id: String = "",
    val userId: String = "",
    val productTypeId: Int = 1,
    val productTypeName: String = "",
    val description: String = "",
    val budget: String = "",
    val widthCm: String = "",
    val heightCm: String = "",
    val depthCm: String = "",
    val comment: String = "",
    val imageUrls: List<String> = emptyList(),
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)


