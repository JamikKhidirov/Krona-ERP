package com.example.client.repository

import android.net.Uri
import com.example.client.data.order.Order
import com.example.client.data.order.OrderStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton




@Singleton
class OrderRepository @Inject constructor() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val ordersCollection = firestore.collection("orders")


    suspend fun createOrder(order: Order, imageUris: List<Uri>): Result<String> = try {
        // Загружаем фото в Storage
        val imageUrls = imageUris.map { uri ->
            uploadImage(uri)
        }

        val orderWithImages = order.copy(imageUrls = imageUrls)
        val docRef = ordersCollection.add(orderWithImages).await()

        // Обновляем ID документа
        docRef.update("id", docRef.id).await()

        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }


    // Получение заказов пользователя
    fun getUserOrders(userId: String): Flow<List<Order>> {
        return ordersCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Order::class.java)
            }
    }

    // Получение заказа по ID
    suspend fun getOrderById(orderId: String): Order? {
        return ordersCollection.document(orderId).get().await()
            .toObject(Order::class.java)
    }


    // Обновление статуса
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        ordersCollection.document(orderId)
            .update("status", status.name)
            .await()
    }


    private suspend fun uploadImage(uri: Uri): String {
        val filename = UUID.randomUUID().toString()
        val ref = storage.reference.child("orders/$filename")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}