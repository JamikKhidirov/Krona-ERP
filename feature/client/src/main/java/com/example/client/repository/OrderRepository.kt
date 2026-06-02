package com.example.client.repository

import android.content.Context
import android.net.Uri
import com.example.client.data.order.Order
import com.example.client.screens.chats.ChatThread
import com.example.client.util.ImageUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private val ordersCollection = firestore.collection("orders")

    suspend fun createOrder(order: Order, imageUris: List<Uri> = emptyList()): Result<String> {
        return try {
            if (order.userId.isBlank()) {
                return Result.failure(IllegalArgumentException("userId пустой"))
            }

            val docRef = ordersCollection.document()
            val orderId = docRef.id

            val base64Images = convertToBase64(imageUris)

            val finalOrder = order.copy(
                id = orderId,
                imageUrls = base64Images,
                createdAt = System.currentTimeMillis(),
                status = "PENDING"
            )

            docRef.set(finalOrder.toMap()).await()

            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun convertToBase64(imageUris: List<Uri>): List<String> = withContext(Dispatchers.IO) {
        if (imageUris.isEmpty()) return@withContext emptyList()
        imageUris.mapNotNull { uri ->
            try {
                ImageUtils.uriToBase64(context, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
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

    suspend fun getChatThreads(userId: String): Result<List<ChatThread>> = try {
        val orders = ordersCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val threads = orders.documents.mapNotNull { doc ->
            val order = doc.toObject(Order::class.java)?.copy(id = doc.id) ?: return@mapNotNull null
            val chatDocs = ordersCollection
                .document(doc.id)
                .collection("chat")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
            val lastMsg = chatDocs.documents.firstOrNull()?.toObject(com.example.client.screens.orderdetailscreen.data.ChatMessage::class.java)
            if (lastMsg != null) {
                ChatThread(
                    orderId = doc.id,
                    orderTitle = order.title,
                    clientName = "",
                    lastMessage = lastMsg.text,
                    lastMessageTime = lastMsg.timestamp,
                    unreadCount = 0
                )
            } else null
        }

        Result.success(threads)
    } catch (e: Exception) {
        Result.failure(e)
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
