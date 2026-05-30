package com.example.navigation.graphs.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.manager.screens.clentscreen.ClientsScreen
import com.example.manager.screens.dashboard.uikit.DashboardScreen
import com.example.manager.screens.orderdetail.ManagerOrderDetailScreen
import com.example.manager.screens.orders.ManagerOrdersScreen
import com.example.manager.screens.ManagerDestinations
import com.example.manager.screens.userdetailscreen.UserDetailScreen
import com.example.manager.uikit.bottombar.ManagerBottomNavigation
import com.example.navigation.graphs.auth.destinations.AuthDestinations
import com.example.network.UserProfile
import com.example.uikit.screens.MyProfileScreen
import com.example.uikit.screens.viewmodel.ThemeViewModel


fun NavGraphBuilder.managerGraph(
    navController: NavHostController
){

    navigation<ManagerDestinations.ManagerGraphDestinaion>(
        startDestination = ManagerDestinations.ClentsScreenDestination
    ){
        composable<ManagerDestinations.ClentsScreenDestination>{
            ClientsScreen(
                onClientClick = {client ->
                    navController.navigate(
                        ManagerDestinations.ClientDetailScreenDestination(client)
                    )
                },
                onSettingsClick = {},
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
                onCallClick = {},
                onMessageClick = {}
            )
        }

        composable<ManagerDestinations.OrdersScreenDestination>{
            ManagerOrdersScreen(
                navController = navController,
                onOrderClick = { order ->
                    navController.navigate(ManagerDestinations.OrderDetailScreenDestination(order))
                }
            )
        }

        composable<ManagerDestinations.OrderDetailScreenDestination> {backStackEntry ->
            val destination = backStackEntry.toRoute<ManagerDestinations.OrderDetailScreenDestination>()

            ManagerOrderDetailScreen(
                orderId = destination.orderId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<ManagerDestinations.DashboardScreenDestination> {
            DashboardScreen()
        }

        composable<ManagerDestinations.ProfileScreenDestination> {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            MyProfileScreen(
                navController = navController,
                onNavigateToAuth = {
                    navController.navigate(AuthDestinations.AuthDestinationGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                bottomBar = {navController
                    ManagerBottomNavigation(navController = navController)
                },
                isDarkMode = isDarkMode,
                onToggleDarkMode = { themeViewModel.toggleDarkMode() }
            )
        }
    }
}
