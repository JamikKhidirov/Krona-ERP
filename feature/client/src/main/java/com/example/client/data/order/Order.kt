package com.example.client.data.order





data class Order(
    val id: String = "",
    val userId: String = "",
    val productTypeId: Int = 1,
    val productTypeName: String = "",
    val description: String = "",
    val budget: String = "",
    val widthCm: String = "",
    val heightCm: String = "",
    val depthCm: String = "",
    val imageUrls: List<String> = emptyList(),
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)



enum class OrderStatus {
    PENDING,       // Ожидает мастера
    IN_PROGRESS,   // В работе
    COMPLETED,     // Выполнен
    CANCELLED      // Отменён
}
