package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.navigation.graphs.auth.authGraph
import com.example.navigation.graphs.client.clientGraph
import com.example.navigation.graphs.manager.managerGraph


@Composable
fun KronaNavHost(
    navController: NavHostController,
    startDestination: Any,
    viewModel: AppNavigationViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Граф авторизации
        authGraph(
            navController = navController,
            onAuthSuccess = { role ->
                viewModel.onAuthSuccess(role)
            }
        )

        // Граф клиента
        clientGraph(
            navController = navController,
        )

        // Граф менеджера
       managerGraph(
           navController = navController
       )
    }
}