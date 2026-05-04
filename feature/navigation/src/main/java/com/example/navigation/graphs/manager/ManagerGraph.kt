package com.example.navigation.graphs.manager

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.client.screens.orderdetailscreen.OrderDetailScreen
import com.example.manager.screens.clentscreen.ClientsScreen
import com.example.manager.screens.orders.ManagerOrdersScreen
import com.example.manager.screens.ManagerDestinations



fun NavGraphBuilder.managerGraph(
    navController: NavHostController
){


    navigation<ManagerDestinations.ManagerGraphDestinaion>(
        startDestination = ManagerDestinations.ClentsScreenDestination
    ){
        composable<ManagerDestinations.ClentsScreenDestination>{
            //Экран всех клиентов
            ClientsScreen(
                onClientClick = {client ->
                    //Тут можно открыть экран клиента для подробного ознакомления с ним
                    navController.navigate(
                        ManagerDestinations.ClientDetailScreenDestination(client)
                    )
                },
                onSettingsClick = {
                    //Открытие экрана настройки если он есть
                },
                navController
            )
        }

        composable<ManagerDestinations.OrdersScreenDestination>{
            //Экран всех заказов
            ManagerOrdersScreen(
                navController = navController,
                onOrderClick = { order ->
                    //Тут можно открыть экран детального просмотра заказа

                    navController.navigate(ManagerDestinations.OrderDetailScreenDestination(order))
                }
            )
        }

        composable<ManagerDestinations.OrderDetailScreenDestination> {backStackEntry ->
            val destination = backStackEntry.toRoute<ManagerDestinations.OrderDetailScreenDestination>()

            OrderDetailScreen(
                orderId = destination.orderId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}