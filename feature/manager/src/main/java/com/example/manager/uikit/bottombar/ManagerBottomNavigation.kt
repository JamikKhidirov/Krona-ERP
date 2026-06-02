package com.example.manager.uikit.bottombar




import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
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
            icon = Icons.AutoMirrored.Filled.List,
            route = ManagerDestinations.OrdersScreenDestination
        ),
        ManagerBottomNavItem(
            label = "Статистика",
            icon = Icons.Default.BarChart,
            route = ManagerDestinations.StatisticsScreenDestination
        ),

        ManagerBottomNavItem(
            label = "Чаты",
            icon = Icons.Default.Chat,
            route = ManagerDestinations.ChatListScreenDestination
        ),
        ManagerBottomNavItem(
            label = "Профиль",
            icon = Icons.Default.Person,
            route = ManagerDestinations.ProfileScreenDestination
        )
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
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
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
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
