package com.example.manager.screens.clentscreen.repository


import com.example.manager.data.Order
import com.example.uikit.Client
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class ClientRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val clientsCollection = firestore.collection("users")
    private val ordersCollection = firestore.collection("orders")

    // Все клиенты в реальном времени
    fun getAllClients(): Flow<List<Client>> = callbackFlow {
        try {
            val listener = clientsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        android.util.Log.e("Firestore", "Ошибка загрузки клиентов: ${error.message}")
                        trySend(emptyList())
                        return@addSnapshotListener
                    }

                    val clients = snapshot?.documents?.map { doc ->
                        doc.toObject(Client::class.java)?.copy(id = doc.id) ?: Client(id = doc.id)
                    } ?: emptyList()

                    trySend(clients)
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            android.util.Log.e("Firestore", "Критическая ошибка: ${e.message}")
            trySend(emptyList())
            close()
        }
    }

    // Поиск клиентов по имени/телефону
    fun searchClients(query: String): Flow<List<Client>> = callbackFlow {
        if (query.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        try {
            val listener = clientsCollection
                .orderBy("lastName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        android.util.Log.e("Firestore", "Ошибка поиска: ${error.message}")
                        trySend(emptyList())  // ← НЕ close(error), а пустой список
                        return@addSnapshotListener
                    }

                    val clients = snapshot?.documents?.map { doc ->
                        doc.toObject(Client::class.java)?.copy(id = doc.id)
                            ?: Client(id = doc.id)
                    } ?: emptyList()

                    trySend(clients)
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            android.util.Log.e("Firestore", "Критическая ошибка поиска: ${e.message}")
            trySend(emptyList())
            close()
        }
    }
    // Получить клиента по ID
    suspend fun getClientById(clientId: String): Result<Client> = try {
        val doc = clientsCollection.document(clientId).get().await()
        val client = doc.toObject(Client::class.java)?.copy(id = doc.id)

        if (client != null) {
            Result.success(client)
        } else {
            Result.failure(Exception("Клиент не найден"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    } as Result<Client>

    // Получить заказы клиента
    fun getClientOrders(clientId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("userId", clientId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.map { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id)
                        ?: Order(id = doc.id)
                } ?: emptyList()

                trySend(orders)
            }

        awaitClose { listener.remove() }
    }
}