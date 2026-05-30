package com.example.manager.repository

import com.example.manager.data.ChatMessage
import com.example.manager.data.Order
import com.example.manager.data.OrderHistoryItem
import com.example.network.data.OrderStatus
import com.google.firebase.auth.FirebaseAuth
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
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ordersCollection = firestore.collection("orders")

    // ✅ ИСПРАВЛЕНО: Безопасное получение с обработкой null createdAt
    fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // ✅ НЕ закрываем flow — отправляем пустой список и логируем
                    trySend(emptyList())
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

    // ✅ Альтернатива без orderBy (если нет индекса)
    fun getAllOrdersUnsorted(): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Order::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                }?.sortedByDescending { it.createdAt } ?: emptyList()

                trySend(orders)
            }
        awaitClose { listener.remove() }
    }

    // ✅ ИСПРАВЛЕНО: whereEqualTo + orderBy требует составной индекс
    fun getUnassignedOrders(): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("status", "PENDING")
            .whereEqualTo("managerId", "")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
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

    // ✅ Альтернатива без составного индекса
    fun getUnassignedOrdersSimple(): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Order::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                }?.filter { it.managerId.isBlank() }
                    ?.sortedByDescending { it.createdAt } ?: emptyList()

                trySend(orders)
            }
        awaitClose { listener.remove() }
    }

    // ✅ ИСПРАВЛЕНО: whereIn + orderBy требует индекс
    fun getMyOrders(managerId: String): Flow<List<Order>> = callbackFlow {
        if (managerId.isBlank()) {
            trySend(emptyList())
            return@callbackFlow
        }

        val listener = ordersCollection
            .whereEqualTo("managerId", managerId)
            .whereIn("status", listOf("ASSIGNED", "IN_PROGRESS", "READY", "DELIVERING"))
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
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

    // ✅ Альтернатива без whereIn (проще индекс)
    fun getMyOrdersSimple(managerId: String): Flow<List<Order>> = callbackFlow {
        if (managerId.isBlank()) {
            trySend(emptyList())
            return@callbackFlow
        }

        val listener = ordersCollection
            .whereEqualTo("managerId", managerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Order::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                }?.filter { it.status in listOf("ASSIGNED", "IN_PROGRESS", "READY", "DELIVERING") }
                    ?.sortedByDescending { it.updatedAt } ?: emptyList()

                trySend(orders)
            }
        awaitClose { listener.remove() }
    }

    // ==================== ДЕЙСТВИЯ ====================

    suspend fun assignOrder(orderId: String, managerId: String, managerName: String): Result<Unit> = try {
        val currentUser = auth.currentUser ?: throw SecurityException("Не авторизован")

        firestore.runTransaction { transaction ->
            val docRef = ordersCollection.document(orderId)
            val snapshot = transaction.get(docRef)

            val currentManagerId = snapshot.getString("managerId") ?: ""
            val currentStatus = snapshot.getString("status") ?: ""

            if (currentManagerId.isNotBlank()) {
                throw IllegalStateException("Заказ уже назначен другому менеджеру")
            }
            if (currentStatus != "PENDING") {
                throw IllegalStateException("Заказ уже в работе")
            }

            transaction.update(docRef, mapOf(
                "managerId" to managerId,
                "managerName" to managerName,
                "status" to "ASSIGNED",
                "assignedAt" to System.currentTimeMillis(),
                "updatedAt" to System.currentTimeMillis()
            ))
        }.await()

        addHistoryEntry(orderId, "ASSIGNED", managerName, "Менеджер взял заказ в работу").onFailure { }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateOrderStatus(
        orderId: String,
        newStatus: OrderStatus,
        comment: String = ""
    ): Result<Unit> {
        return try {
            val managerId = auth.currentUser?.uid ?: throw SecurityException("Не авторизован")

            val orderDoc = ordersCollection.document(orderId).get().await()
            val orderManagerId = orderDoc.getString("managerId") ?: ""
            val managerName = orderDoc.getString("managerName") ?: "Менеджер"

            if (orderManagerId != managerId) {
                return Result.failure(SecurityException("Заказ назначен другому менеджеру"))
            }

            val updates = mutableMapOf<String, Any>(
                "status" to newStatus.name,
                "updatedAt" to System.currentTimeMillis()
            )

            if (comment.isNotBlank()) {
                updates["managerComment"] = comment
            }

            if (newStatus == OrderStatus.COMPLETED) {
                updates["completedAt"] = System.currentTimeMillis()
            }

            ordersCollection.document(orderId).update(updates).await()

            val statusLabel = when (newStatus) {
                OrderStatus.IN_PROGRESS -> "В работе"
                OrderStatus.READY -> "Готов к выдаче"
                OrderStatus.DELIVERING -> "Доставляется"
                OrderStatus.COMPLETED -> "Завершён"
                OrderStatus.CANCELLED -> "Отменён"
                else -> newStatus.name
            }
            addHistoryEntry(orderId, newStatus.name, managerName, comment.ifBlank { "Статус изменён: $statusLabel" })

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addManagerComment(orderId: String, comment: String): Result<Unit> = try {
        ordersCollection.document(orderId)
            .update(
                "managerComment", comment,
                "updatedAt", System.currentTimeMillis()
            ).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateOrderDetails(
        orderId: String,
        budget: String? = null,
        material: String? = null,
        title: String? = null
    ): Result<Unit> = try {
        val updates = mutableMapOf<String, Any>(
            "updatedAt" to System.currentTimeMillis()
        )

        budget?.let { updates["budget"] = it }
        material?.let { updates["material"] = it }
        title?.let { updates["title"] = it }

        ordersCollection.document(orderId).update(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val doc = ordersCollection.document(orderId).get().await()
            if (!doc.exists()) {
                return Result.failure(Exception("Заказ не найден"))
            }
            val order = doc.toObject(Order::class.java)?.copy(id = doc.id)
                ?: return Result.failure(Exception("Ошибка парсинга заказа"))
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getOrderHistoryFlow(orderId: String): Flow<List<OrderHistoryItem>> = callbackFlow {
        val listener = ordersCollection
            .document(orderId)
            .collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val history = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(OrderHistoryItem::class.java)
                } ?: emptyList()
                trySend(history)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addHistoryEntry(
        orderId: String,
        status: String,
        managerName: String,
        comment: String = ""
    ): Result<Unit> = try {
        val managerId = auth.currentUser?.uid ?: throw SecurityException("Не авторизован")
        val historyRef = ordersCollection
            .document(orderId)
            .collection("history")
            .document()

        val entry = mapOf(
            "status" to status,
            "managerId" to managerId,
            "managerName" to managerName,
            "comment" to comment,
            "timestamp" to System.currentTimeMillis()
        )
        historyRef.set(entry).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getOrderHistory(orderId: String): Result<List<OrderHistoryItem>> = try {
        val historyCollection = ordersCollection
            .document(orderId)
            .collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        val history = historyCollection.documents.mapNotNull { doc ->
            doc.toObject(OrderHistoryItem::class.java)
        }

        Result.success(history)
    } catch (e: Exception) {
        Result.failure(e)
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
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
