package com.example.manager

import com.example.manager.data.OrderHistoryItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class OrderHistoryTest {
    @Test
    fun orderHistoryItem_creation() {
        val item = OrderHistoryItem(
            id = "1",
            status = "ASSIGNED",
            managerId = "mgr1",
            managerName = "Иван",
            comment = "Взял в работу",
            timestamp = 1000L
        )
        assertEquals("ASSIGNED", item.status)
        assertEquals("Иван", item.managerName)
        assertEquals("Взял в работу", item.comment)
        assertEquals(1000L, item.timestamp)
    }

    @Test
    fun orderHistoryItem_defaults() {
        val item = OrderHistoryItem()
        assertEquals("", item.id)
        assertEquals("", item.status)
        assertEquals("", item.managerName)
        assertNotNull(item.timestamp)
    }

    @Test
    fun orderHistoryItem_differentStatuses() {
        val statuses = listOf("PENDING", "ASSIGNED", "IN_PROGRESS", "READY", "DELIVERING", "COMPLETED", "CANCELLED")
        statuses.forEach { status ->
            val item = OrderHistoryItem(status = status)
            assertEquals(status, item.status)
        }
    }
}
