package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.auth.screens.login.LogInScreen


@Composable
fun KronaAppNavigation(
    isLogin: Boolean
){

    val navController = rememberNavController()



    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LogInScreen(
                onNavigateToRegister = {

                },
                onLoginSuccess ={

                }
            )
        }
        composable("main/{role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""

        }
    }
}