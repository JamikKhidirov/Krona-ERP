package com.example.manager.data

data class DashboardStats(
    val totalOrders: Int = 0,
    val pendingOrders: Int = 0,
    val inProgressOrders: Int = 0,
    val completedOrders: Int = 0,
    val cancelledOrders: Int = 0,
    val overdueOrders: Int = 0,
    val monthlyProfit: Double = 0.0
)
