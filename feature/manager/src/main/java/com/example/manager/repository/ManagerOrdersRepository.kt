package com.example.manager.repository

import com.example.manager.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ManagerOrdersRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
){

    private val ordersCollection = firestore.collection("orders")



    // Все заказы в реальном времени
    fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        try {
            val listener = ordersCollection
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Не крашимся — отправляем пустой список + лог
                        android.util.Log.e("Firestore", "Ошибка загрузки заказов: ${error.message}")
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val orders = snapshot?.documents?.map { doc ->
                        doc.toObject(Order::class.java)?.copy(id = doc.id) ?: Order(id = doc.id)
                    } ?: emptyList()

                    trySend(orders)
                }

            //Закрываем поток
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            android.util.Log.e("Firestore", "Критическая ошибка: ${e.message}")
            trySend(emptyList())
            close()
        }
    }

    // Фильтр по статусу
    fun getOrdersByStatus(status: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("status", status)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.map { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id) ?: Order(id = doc.id)
                } ?: emptyList()

                trySend(orders)
            }

        awaitClose { listener.remove() }
    }

    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = try {
        ordersCollection.document(orderId)
            .update("status", status)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

}