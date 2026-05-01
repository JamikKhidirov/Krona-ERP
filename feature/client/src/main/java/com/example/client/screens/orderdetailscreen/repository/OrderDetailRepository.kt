package com.example.client.screens.orderdetailscreen.repository

import com.example.client.screens.orderdetailscreen.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class OrderDetailRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val ordersCollection = firestore.collection("orders")

    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val doc = ordersCollection.document(orderId).get().await()

            if (!doc.exists()) {
                return Result.failure(Exception("Заказ не найден"))
            }

            // Ручной маппинг — надёжнее toObject()
            val order = Order(
                id = doc.id,
                userId = doc.getString("userId") ?: "",
                productTypeId = doc.getLong("productTypeId")?.toInt() ?: 1,
                productTypeName = doc.getString("productTypeName") ?: "",
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                budget = doc.getString("budget") ?: "",
                widthCm = doc.getString("widthCm") ?: "",
                heightCm = doc.getString("heightCm") ?: "",
                depthCm = doc.getString("depthCm") ?: "",
                imageUrls = doc.get("imageUrls") as? List<String> ?: emptyList(),
                status = doc.getString("status") ?: "PENDING",
                createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                deadline = doc.getString("deadline") ?: "",
                material = doc.getString("material") ?: "",
                color = doc.getString("color") ?: "",
                facade = doc.getString("facade") ?: "",
                masterId = doc.getString("masterId") ?: "",
                masterName = doc.getString("masterName") ?: "",
                masterPhone = doc.getString("masterPhone") ?: "",
                masterPhotoUrl = doc.getString("masterPhotoUrl") ?: "",
                masterRating = doc.getDouble("masterRating") ?: 0.0,
                paidAmount = doc.getString("paidAmount") ?: "0",
                comment = doc.getString("comment") ?: ""
            )

            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}