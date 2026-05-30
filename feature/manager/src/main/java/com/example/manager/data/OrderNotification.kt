package com.example.manager.data

import com.google.firebase.firestore.PropertyName

data class OrderNotification(
    @PropertyName("id") val id: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("orderId") val orderId: String = "",
    @PropertyName("title") val title: String = "",
    @PropertyName("body") val body: String = "",
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("read") val read: Boolean = false
)
