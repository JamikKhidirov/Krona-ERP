package com.example.client.screens.orderdetailscreen.data

import com.google.firebase.firestore.PropertyName


data class StatusUpdate(
    @PropertyName("status")
    val status: String = "",

    @PropertyName("date")
    val date: String = "",

    @PropertyName("comment")
    val comment: String = ""
)