package com.example.navigation.graphs.client

import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.client.screens.myorders.MyOrdersScreen
import com.example.client.screens.neworder.NewOrderScreen
import com.example.client.screens.orderdetailscreen.OrderDetailScreen
import com.example.navigation.graphs.client.destinations.ClientDestinations
import com.example.navigation.graphs.manager.destinations.ManagerDestinations


fun NavGraphBuilder.clientGraph(
    navController: NavHostController
){

    navigation<ClientDestinations.ClientGraph>(
        startDestination = ClientDestinations.MyOrdersScreenDestination
    ){
        composable<ClientDestinations.MyOrdersScreenDestination>{
            MyOrdersScreen(
                onOrderClick = {order ->
                    //Приходит товар надо навиировать на экран DetailOrder
                    navController.navigate(ManagerDestinations.OrderDetailScreenDestination(order))

                }
            )
        }

        composable<ClientDestinations.OrderDetailScreenDestinations> { backStackEntry ->

            val destination = backStackEntry.toRoute<ManagerDestinations.OrderDetailScreenDestination>()
            OrderDetailScreen(
                orderId = destination.orderId,
                onNavigateBack = {
                    //Обратно навигируемся на экран Мои заказы
                    navController.popBackStack()
                }
            )
        }

        composable<ClientDestinations.NewOrderScreenDestinations>{
            NewOrderScreen()
        }
    }

}