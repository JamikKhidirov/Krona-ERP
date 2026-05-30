package com.example.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel


import androidx.navigation.compose.rememberNavController
import com.example.auth.screens.login.LogInScreen
import com.example.navigation.graphs.auth.destinations.AuthDestinations
import com.example.manager.screens.ManagerDestinations
import com.example.uikit.ClientDestinations


@Composable
fun KronaAppNavigation(
    viewModel: AppNavigationViewModel = hiltViewModel()
){

    val navController = rememberNavController()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Состояние навигации
    val navigationState by viewModel.navigationState.collectAsState()

    // Экран загрузки пока проверяем авторизацию
    if (navigationState is NavigationState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF25326A))
        }
        return
    }

    // Определяем стартовый экран
    val startDestination = when (navigationState) {
        is NavigationState.Auth -> AuthDestinations.AuthDestinationGraph
        is NavigationState.Client -> ClientDestinations.ClientGraph
        is NavigationState.Manager -> ManagerDestinations.ManagerGraphDestinaion
        else -> AuthDestinations.AuthDestinationGraph
    }


    // Настраиваем навигацию при изменении состояния
    LaunchedEffect(navigationState) {
        when (val state = navigationState) {
            is NavigationState.Auth -> {
                navController.navigate(AuthDestinations.AuthDestinationGraph) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is NavigationState.Client -> {
                navController.navigate(ClientDestinations.ClientGraph) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is NavigationState.Manager -> {
                navController.navigate(ManagerDestinations.ManagerGraphDestinaion) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    KronaNavHost(
        navController = navController,
        startDestination = startDestination,
        viewModel = viewModel
    )
}