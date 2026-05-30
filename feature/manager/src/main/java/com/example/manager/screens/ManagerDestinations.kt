package com.example.manager.screens

import com.example.uikit.ClientDestinations
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
    data class ClientDetailScreenDestination(val clientId: String): ManagerDestinations()


    @Serializable
    data class OrderDetailScreenDestination(val orderId: String): ManagerDestinations()



    @Serializable
    object ProfileScreenDestination: ManagerDestinations()


    //Адрес экрана для списков заказов
    @Serializable
    data object OrdersScreenDestination: ManagerDestinations()

    //Адрес экрана дэшборда
    @Serializable
    data object DashboardScreenDestination: ManagerDestinations()

    //Адрес экрана статистики
    @Serializable
    data object StatisticsScreenDestination: ManagerDestinations()
}