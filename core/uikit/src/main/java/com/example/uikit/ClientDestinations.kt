package com.example.uikit

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientDestinations {



    //Адрес графа клиента тоесть его route
    @Serializable
    data object ClientGraph: ClientDestinations()


    //Экран мои заказы
    @Serializable
    data object MyOrdersScreenDestination: ClientDestinations()


    //Экран создания нового заказа
    @Serializable
    data object NewOrderScreenDestinations: ClientDestinations()


    @Serializable
    object ProfileScreenDestination: ClientDestinations()


    //Экран детального просмотра заказа
    @Serializable
    data class OrderDetailScreenDestinations(val orderId: String): ClientDestinations()
}