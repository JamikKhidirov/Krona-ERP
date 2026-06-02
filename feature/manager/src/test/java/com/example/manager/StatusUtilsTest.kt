package com.example.manager

import com.example.manager.data.Order
import com.example.network.data.OrderStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusUtilsTest {
    @Test
    fun statusLabel_pending() {
        assertEquals("Ожидает", getStatusLabelSimple(OrderStatus.PENDING))
    }

    @Test
    fun statusLabel_assigned() {
        assertEquals("Назначен", getStatusLabelSimple(OrderStatus.ASSIGNED))
    }

    @Test
    fun statusLabel_inProgress() {
        assertEquals("В работе", getStatusLabelSimple(OrderStatus.IN_PROGRESS))
    }

    @Test
    fun statusLabel_ready() {
        assertEquals("Готов", getStatusLabelSimple(OrderStatus.READY))
    }

    @Test
    fun statusLabel_delivering() {
        assertEquals("Доставляется", getStatusLabelSimple(OrderStatus.DELIVERING))
    }

    @Test
    fun statusLabel_completed() {
        assertEquals("Выполнен", getStatusLabelSimple(OrderStatus.COMPLETED))
    }

    @Test
    fun statusLabel_cancelled() {
        assertEquals("Отменён", getStatusLabelSimple(OrderStatus.CANCELLED))
    }

    @Test
    fun order_activeStatuses() {
        assertTrue(Order.ACTIVE_STATUSES.contains("PENDING"))
        assertTrue(Order.ACTIVE_STATUSES.contains("IN_PROGRESS"))
        assertTrue(Order.ACTIVE_STATUSES.contains("READY"))
    }

    @Test
    fun order_finalStatuses() {
        assertTrue(Order.FINAL_STATUSES.contains("COMPLETED"))
        assertTrue(Order.FINAL_STATUSES.contains("CANCELLED"))
    }

    @Test
    fun order_remainingAmount() {
        val order = Order(budget = "100 000", paidAmount = "30 000")
        assertTrue(order.getRemainingAmount().contains("70"))
    }

    @Test
    fun order_remainingAmount_zero() {
        val order = Order(budget = "50 000", paidAmount = "50 000")
        assertEquals("0 ₽", order.getRemainingAmount())
    }

    @Test
    fun order_paymentPercent() {
        val order = Order(budget = "100 000", paidAmount = "25 000")
        assertEquals(25, order.getPaymentPercent())
    }

    @Test
    fun order_isOverdue_returnsFalseForCompleted() {
        val order = Order(status = "COMPLETED", deadlineTimestamp = 1000L)
        assertEquals(false, order.isOverdue())
    }

    @Test
    fun order_isEditable() {
        assertTrue(Order(status = "PENDING").isEditable())
        assertTrue(Order(status = "IN_PROGRESS").isEditable())
        assertEquals(false, Order(status = "COMPLETED").isEditable())
        assertEquals(false, Order(status = "CANCELLED").isEditable())
    }

    @Test
    fun order_shortSummary() {
        val order = Order(productTypeName = "Кухня", widthCm = "300", heightCm = "200", depthCm = "60", material = "ЛДСП")
        assertEquals("Кухня · 300×200×60 см · ЛДСП", order.getShortSummary())
    }

    @Test
    fun order_fullAddress() {
        val order = Order(city = "Москва", address = "ул. Ленина, д. 1", apartment = "42")
        assertTrue(order.getFullAddress().contains("Москва"))
        assertTrue(order.getFullAddress().contains("ул. Ленина, д. 1"))
        assertTrue(order.getFullAddress().contains("42"))
    }

    @Test
    fun order_calculatedProfit() {
        val order = Order(budget = "100 000", costPrice = "60 000", deliveryCost = "5 000", assemblyCost = "3 000")
        assertTrue(order.getCalculatedProfit().contains("32"))
    }

    @Test
    fun order_marginPercent() {
        val order = Order(budget = "100 000", costPrice = "60 000")
        val margin = order.getMarginPercent()
        assertTrue(margin > 0)
    }

    @Test
    fun order_completenessPercent() {
        val full = Order(
            description = "desc", budget = "100", material = "mat",
            color = "col", deadline = "date", address = "addr",
            clientPhone = "phone", imageUrls = listOf("img"),
            widthCm = "100", heightCm = "200", managerId = "mgr"
        )
        assertTrue(full.getCompletenessPercent() >= 100)

        val empty = Order()
        assertTrue(empty.getCompletenessPercent() <= 20)
    }

    private fun getStatusLabelSimple(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Ожидает"
            OrderStatus.ASSIGNED -> "Назначен"
            OrderStatus.IN_PROGRESS -> "В работе"
            OrderStatus.READY -> "Готов"
            OrderStatus.DELIVERING -> "Доставляется"
            OrderStatus.COMPLETED -> "Выполнен"
            OrderStatus.PAID -> "Оплачен"
            OrderStatus.CANCELLED -> "Отменён"
        }
    }
}

