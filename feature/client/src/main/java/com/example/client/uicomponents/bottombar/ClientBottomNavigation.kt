package com.example.client.uicomponents.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.client.screens.ClientDestinations


@Composable
fun ClientBottomNavigation(
    navController: NavHostController
) {
    val items = listOf(
        ClientBottomNavItem(
            label = "Мои заказы",
            icon = Icons.Default.List, // или Icons.Default.ShoppingBag
            route = ClientDestinations.MyOrdersScreenDestination
        ),
        ClientBottomNavItem(
            label = "Новый заказ",
            icon = Icons.Default.Add, // или Icons.Default.Create
            route = ClientDestinations.NewOrderScreenDestinations
        )
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF6366F1)
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hasRoute(item.route::class) == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) Color(0xFF6366F1) else Color(0xFF94A3B8)
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6366F1),
                    selectedTextColor = Color(0xFF6366F1),
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8),
                    indicatorColor = Color(0xFFEEF2FF)
                )
            )
        }
    }
}


data class ClientBottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)