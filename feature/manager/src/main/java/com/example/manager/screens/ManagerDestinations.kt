package com.example.manager.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class ManagerDestinations() {


    //Адрес графы для менеджера тоесть это адрес на которм начинаеться экраны менеджера
    @Serializable
    data object ManagerGraphDestinaion: ManagerDestinations()


    //Адрес экрана для всех клиентов
    @Serializable
    data object ClentsScreenDestination: ManagerDestinations()


    @Serializable
    data class ClientDetailScreenDestination(val clientId: String)


    @Serializable
    data class OrderDetailScreenDestination(val orderId: String)



    //Адрес экрана для списков заказов
    @Serializable
    data object OrdersScreenDestination: ManagerDestinations()
}