package com.example.manager.uikit.bottombar




import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.manager.screens.ManagerDestinations
import com.example.uikit.ClientDestinations
import com.example.uikit.uikit.ClientBottomNavItem


@Composable
@Preview(showBackground = true)
fun ManagerBottomNavigation(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        ManagerBottomNavItem(
            label = "Клиенты",
            icon = Icons.Default.People,
            route = ManagerDestinations.ClentsScreenDestination
        ),
        ManagerBottomNavItem(
            label = "Заказы",
            icon = Icons.Default.List,
            route = ManagerDestinations.OrdersScreenDestination
        ),

        ManagerBottomNavItem(
            label = "Профиль",
            icon = Icons.Default.Person,
            route = ClientDestinations.ProfileScreenDestination
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
                        // Очищаем стек до стартового экрана
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Избегаем дублирования
                        launchSingleTop = true
                        // Восстанавливаем состояние
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


data class ManagerBottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)
