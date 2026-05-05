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
import com.example.manager.screens.userdetailscreen.UserDetailScreen
import com.example.manager.uikit.bottombar.ManagerBottomNavigation
import com.example.navigation.graphs.auth.destinations.AuthDestinations
import com.example.network.UserProfile
import com.example.uikit.screens.MyProfileScreen


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

        composable<ManagerDestinations.ClientDetailScreenDestination> {backStackEntry ->
            val destination = backStackEntry.toRoute<ManagerDestinations.ClientDetailScreenDestination>()
            UserDetailScreen(
                navController = navController,
                clientId = destination.clientId,
                onBackClick = {
                    navController.popBackStack()
                },
                onOrderClick = { orderId ->
                    navController.navigate(
                        ManagerDestinations.OrderDetailScreenDestination(orderId)
                    )
                },
                onCallClick = { phone ->
                    // Intent на звонок
                },
                onMessageClick = { phone ->
                    // Intent на сообщение
                }

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


        composable<ManagerDestinations.ProfileScreenDestination> {
            MyProfileScreen(
                navController = navController,
                onNavigateToAuth = {
                    navController.navigate(AuthDestinations.AuthDestinationGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                bottomBar = {navController
                    ManagerBottomNavigation(navController = navController)
                }
            )
            }
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
