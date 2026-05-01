package com.example.client.screens.orderdetailscreen.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun formatDate(date: Date): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
}