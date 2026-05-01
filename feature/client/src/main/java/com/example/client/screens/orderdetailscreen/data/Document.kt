package com.example.client.screens.orderdetailscreen.data

import com.google.firebase.firestore.PropertyName


data class Document(
    @PropertyName("name")
    val name: String = "",

    @PropertyName("url")
    val url: String = "",

    @PropertyName("size")
    val size: String = ""
)