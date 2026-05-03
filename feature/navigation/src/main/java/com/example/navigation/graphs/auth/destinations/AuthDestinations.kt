package com.example.navigation.graphs.auth.destinations

import kotlinx.serialization.Serializable





@Serializable
sealed class AuthDestinations {


    //Адрес самого графа Auth (Граф авторизации и регистрации)
    @Serializable
    data object AuthDestinationGraph: AuthDestinations()



    //Экран входа в графе Auth
    @Serializable
    data object LogInScreenDestination: AuthDestinations()



    //Экран регистрации в графе Auth
    @Serializable
    data object RegisterScreenDestination: AuthDestinations()



}