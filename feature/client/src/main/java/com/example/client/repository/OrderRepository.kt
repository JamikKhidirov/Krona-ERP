package com.example.client.repository

import android.net.Uri
import com.example.client.data.order.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val ordersCollection = firestore.collection("orders")

    suspend fun createOrder(order: Order, imageUris: List<Uri> = emptyList()): Result<String> {
        return try {
            if (order.userId.isBlank()) {
                return Result.failure(IllegalArgumentException("userId пустой"))
            }

            val docRef = ordersCollection.document()
            val orderId = docRef.id

            val uploadedUrls = uploadImages(orderId, imageUris)

            val finalOrder = order.copy(
                id = orderId,
                imageUrls = uploadedUrls,
                createdAt = System.currentTimeMillis(),
                status = "PENDING"
            )

            docRef.set(finalOrder.toMap()).await()

            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImages(orderId: String, imageUris: List<Uri>): List<String> {
        if (imageUris.isEmpty()) return emptyList()
        val storageRef = storage.reference.child("order_images/$orderId")
        val urls = mutableListOf<String>()
        for ((index, uri) in imageUris.withIndex()) {
            try {
                val imageRef = storageRef.child("${UUID.randomUUID()}.jpg")
                imageRef.putFile(uri).await()
                val downloadUrl = imageRef.downloadUrl.await()
                urls.add(downloadUrl.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return urls
    }

    fun getUserOrders(userId: String): Flow<List<Order>> = callbackFlow {
        if (userId.isBlank()) {
            close(IllegalArgumentException("userId пустой"))
            return@callbackFlow
        }

        val listener = ordersCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Order::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(orders)
            }

        awaitClose { listener.remove() }
    }


    fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(orders)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getOrderById(orderId: String): Result<Order> = try {
        val doc = ordersCollection.document(orderId).get().await()
        val order = doc.toObject(Order::class.java)?.copy(id = doc.id)

        if (order != null) {
            Result.success(order)
        } else {
            Result.failure(NoSuchElementException("Заказ не найден"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun Order.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "userId" to userId,
        "productTypeId" to productTypeId,
        "productTypeName" to productTypeName,
        "description" to description,
        "budget" to budget,
        "widthCm" to widthCm,
        "heightCm" to heightCm,
        "depthCm" to depthCm,
        "comment" to comment,
        "imageUrls" to imageUrls,
        "status" to status,
        "createdAt" to createdAt,
        "updatedAt" to System.currentTimeMillis(),
        "address" to address,
        "city" to city,
        "deliveryType" to deliveryType
    )
}
