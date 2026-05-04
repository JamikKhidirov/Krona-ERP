package com.example.manager.screens.userdetailscreen.core





fun formatDateLong(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("ru"))
    return sdf.format(java.util.Date(timestamp))
}