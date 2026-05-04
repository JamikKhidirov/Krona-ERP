package com.example.manager.repository

import com.example.manager.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ManagerOrdersRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private val ordersCollection = firestore.collection("orders")

    // Все заказы — кэшируем для фильтрации на клиенте
    private val allOrdersFlow: Flow<List<Order>> = callbackFlow {
        val listener = try {
            ordersCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        android.util.Log.e("Firestore", "❌ Ошибка загрузки заказов: ${error.message}")
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    try {
                        val orders = snapshot?.documents?.mapNotNull { doc ->
                            doc.toObject(Order::class.java)?.copy(id = doc.id)
                        } ?: emptyList()

                        android.util.Log.d("Firestore", "✅ Загружено заказов: ${orders.size}")
                        trySend(orders)
                    } catch (e: Exception) {
                        android.util.Log.e("Firestore", "❌ Ошибка парсинга: ${e.message}")
                        trySend(emptyList())
                    }
                }
        } catch (e: Exception) {
            android.util.Log.e("Firestore", "❌ Критическая ошибка listener: ${e.message}")
            trySend(emptyList())
            null
        }

        awaitClose {
            listener?.remove()
            android.util.Log.d("Firestore", "Listener закрыт")
        }
    }.catch { e ->
        android.util.Log.e("Firestore", "❌ Ошибка в Flow: ${e.message}")
        emit(emptyList())
    }

    fun getAllOrders(): Flow<List<Order>> = allOrdersFlow

    // Фильтрация на клиенте — безопасно
    fun getOrdersByStatus(status: String): Flow<List<Order>> = allOrdersFlow.map { orders ->
        try {
            orders.filter { it.status.equals(status, ignoreCase = true) }
        } catch (e: Exception) {
            android.util.Log.e("Filter", "❌ Ошибка фильтрации: ${e.message}")
            emptyList()
        }
    }.catch { e ->
        android.util.Log.e("Filter", "❌ Ошибка в фильтре: ${e.message}")
        emit(emptyList())
    }

    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = try {
        ordersCollection.document(orderId)
            .update("status", status)
            .await()
        android.util.Log.d("Firestore", "✅ Статус обновлён: $orderId -> $status")
        Result.success(Unit)
    } catch (e: Exception) {
        android.util.Log.e("Firestore", "❌ Ошибка обновления: ${e.message}")
        Result.failure(e)
    }
}