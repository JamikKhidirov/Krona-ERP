package com.example.client.screens.orderdetailscreen.data

import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StatusUpdate(
    @PropertyName("status")
    val status: String = "",

    @PropertyName("date")
    val date: String = "",

    @PropertyName("comment")
    val comment: String = "",

    @PropertyName("timestamp")
    val timestamp: Long = 0L,

    @PropertyName("managerName")
    val managerName: String = ""
) {
    val displayDate: String
        get() = if (timestamp > 0L) {
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
        } else {
            date
        }
}
