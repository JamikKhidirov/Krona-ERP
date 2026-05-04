package com.example.client.repository

import android.net.Uri
import com.example.client.data.order.Order
import com.example.network.data.OrderStatus

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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

    suspend fun createOrder(order: Order, imageUris: List<Uri>): Result<String> = try {
        // Загружаем фото в Storage
        val imageUrls = imageUris.map { uri ->
            uploadImage(uri)
        }

        val orderWithImages = order.copy(
            imageUrls = imageUrls,
            createdAt = System.currentTimeMillis()
        )

        // Создаём документ с явным ID или генерируем
        val docRef = ordersCollection.document()
        val finalOrder = orderWithImages.copy(id = docRef.id)

        // Сохраняем целиком объект (включая userId!)
        docRef.set(finalOrder.toMap()).await()

        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Получение заказов пользователя - ПРОВЕРЬТЕ ЭТОТ МЕТОД
    fun getUserOrders(userId: String): Flow<List<Order>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(emptyList())
            close(IllegalArgumentException("userId is blank"))
            return@callbackFlow
        }

        val listener = ordersCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
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

    // Для получения всех заказов (для менеджера)
    fun getAllOrders(): Flow<List<Order>> = ordersCollection
        .orderBy("createdAt", Query.Direction.DESCENDING)
        .snapshots()
        .map { snapshot ->
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(id = doc.id)
            }
        }

    suspend fun getOrderById(orderId: String): Order? {
        return ordersCollection.document(orderId).get().await()
            .toObject(Order::class.java)?.copy(id = orderId)
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        ordersCollection.document(orderId)
            .update("status", status.name)
            .await()
    }

    private suspend fun uploadImage(uri: Uri): String {
        val filename = "orders/${UUID.randomUUID()}_${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(filename)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    // Хелпер для конвертации в Map (гарантирует сохранение всех полей)
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
        "createdAt" to createdAt
    )
}