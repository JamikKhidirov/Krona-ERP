package com.example.navigation.graphs.client

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.client.screens.myorders.MyOrdersScreen
import com.example.client.screens.neworder.NewOrderScreen
import com.example.client.screens.orderdetailscreen.OrderDetailScreen
import com.example.navigation.graphs.auth.destinations.AuthDestinations
import com.example.uikit.ClientDestinations
import com.example.uikit.screens.MyProfileScreen
import com.example.uikit.screens.viewmodel.ThemeViewModel
import com.example.uikit.uikit.ClientBottomNavigation


fun NavGraphBuilder.clientGraph(
    navController: NavHostController
){

    navigation<ClientDestinations.ClientGraph>(
        startDestination = ClientDestinations.MyOrdersScreenDestination
    ){
        composable<ClientDestinations.MyOrdersScreenDestination>{
            MyOrdersScreen(
                navController = navController,
                onOrderClick = { orderId ->
                    navController.navigate(ClientDestinations.OrderDetailScreenDestinations(orderId))
                }
            )
        }

        composable<ClientDestinations.OrderDetailScreenDestinations> { backStackEntry ->
            val destination = backStackEntry.toRoute<ClientDestinations.OrderDetailScreenDestinations>()
            OrderDetailScreen(
                orderId = destination.orderId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<ClientDestinations.ProfileScreenDestination> {
            val activity = LocalContext.current as? ComponentActivity
            val themeViewModel = if (activity != null) {
                hiltViewModel<ThemeViewModel>(viewModelStoreOwner = activity)
            } else {
                hiltViewModel<ThemeViewModel>()
            }
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            MyProfileScreen(
                navController = navController,
                onNavigateToAuth = {},
                bottomBar = {navController
                    ClientBottomNavigation(navController = navController)
                },
                isDarkMode = isDarkMode,
                onToggleDarkMode = { themeViewModel.toggleDarkMode() }
            )
        }


        composable<ClientDestinations.NewOrderScreenDestinations>{
            NewOrderScreen(
                navController = navController
            )
        }
    }

}