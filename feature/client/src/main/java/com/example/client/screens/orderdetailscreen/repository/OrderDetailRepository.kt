package com.example.client.screens.orderdetailscreen.repository

import com.example.client.screens.orderdetailscreen.data.ChatMessage
import com.example.client.screens.orderdetailscreen.data.Order
import com.example.client.screens.orderdetailscreen.data.StatusUpdate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class OrderDetailRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val ordersCollection = firestore.collection("orders")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val doc = ordersCollection.document(orderId).get().await()

            if (!doc.exists()) {
                return Result.failure(Exception("Заказ не найден"))
            }

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
                comment = doc.getString("comment") ?: "",
                managerId = doc.getString("managerId") ?: "",
                managerName = doc.getString("managerName") ?: "",
                managerPhone = doc.getString("managerPhone") ?: "",
                clientRating = doc.getLong("clientRating")?.toInt() ?: 0,
                clientReview = doc.getString("clientReview") ?: ""
            )

            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getChatMessages(orderId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = ordersCollection
            .document(orderId)
            .collection("chat")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ChatMessage::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveReview(orderId: String, rating: Int, review: String): Result<Unit> = try {
        val user = auth.currentUser ?: throw SecurityException("Не авторизован")
        ordersCollection.document(orderId)
            .update(
                mapOf(
                    "clientRating" to rating,
                    "clientReview" to review,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getOrderHistoryFlow(orderId: String): Flow<List<StatusUpdate>> = callbackFlow {
        val listener = ordersCollection
            .document(orderId)
            .collection("history")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val history = snapshot?.documents?.mapNotNull { doc ->
                    val status = doc.getString("status") ?: ""
                    val timestamp = doc.getLong("timestamp") ?: 0L
                    val comment = doc.getString("comment") ?: ""
                    val managerName = doc.getString("managerName") ?: ""
                    StatusUpdate(status = status, timestamp = timestamp, comment = comment, managerName = managerName)
                } ?: emptyList()
                trySend(history)
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendChatMessage(orderId: String, text: String): Result<Unit> = try {
        val user = auth.currentUser ?: throw SecurityException("Не авторизован")
        val chatRef = ordersCollection
            .document(orderId)
            .collection("chat")
            .document()

        val name = user.displayName ?: user.email ?: "Пользователь"
        val message = mapOf(
            "senderId" to user.uid,
            "senderName" to name,
            "text" to text,
            "timestamp" to System.currentTimeMillis()
        )
        chatRef.set(message).await()

        val orderDoc = ordersCollection.document(orderId).get().await()
        val managerUserId = orderDoc.getString("managerId") ?: ""
        if (managerUserId.isNotBlank() && managerUserId != user.uid) {
            writeChatNotification(orderId, managerUserId, name, text)
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun writeChatNotification(orderId: String, userId: String, senderName: String, text: String) {
        try {
            val notifRef = firestore.collection("notifications").document()
            notifRef.set(mapOf(
                "id" to notifRef.id,
                "userId" to userId,
                "orderId" to orderId,
                "title" to "Новое сообщение от $senderName",
                "body" to text,
                "createdAt" to System.currentTimeMillis(),
                "read" to false,
                "type" to "chat"
            )).await()
        } catch (_: Exception) { }
    }
}