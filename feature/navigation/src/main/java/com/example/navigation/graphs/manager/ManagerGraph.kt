package com.example.navigation.graphs.manager

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
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
import com.example.manager.screens.chats.ChatListScreen
import com.example.manager.screens.clentscreen.ClientsScreen
import com.example.manager.screens.dashboard.uikit.DashboardScreen
import com.example.manager.screens.orderdetail.ManagerOrderDetailScreen
import com.example.manager.screens.statistics.uikit.ManagerStatisticsScreen
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
            val ctx = LocalContext.current
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
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    ctx.startActivity(intent)
                },
                onMessageClick = { phone ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phone"))
                    ctx.startActivity(intent)
                }
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
            DashboardScreen(
                navController = navController,
                onNavigateToStatistics = {
                    navController.navigate(ManagerDestinations.StatisticsScreenDestination)
                }
            )
        }

        composable<ManagerDestinations.StatisticsScreenDestination> {
            ManagerStatisticsScreen(
                navController = navController
            )
        }

        composable<ManagerDestinations.ChatListScreenDestination> {
            ChatListScreen(
                onChatClick = { orderId ->
                    navController.navigate(
                        ManagerDestinations.OrderDetailScreenDestination(orderId)
                    )
                },
                bottomBar = {
                    ManagerBottomNavigation(navController = navController)
                }
            )
        }

        composable<ManagerDestinations.ProfileScreenDestination> {
            val activity = LocalContext.current as? ComponentActivity
            val themeViewModel = if (activity != null) {
                hiltViewModel<ThemeViewModel>(viewModelStoreOwner = activity)
            } else {
                hiltViewModel<ThemeViewModel>()
            }
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
