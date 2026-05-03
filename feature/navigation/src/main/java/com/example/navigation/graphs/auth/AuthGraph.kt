package com.example.navigation.graphs.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.auth.screens.login.LogInScreen
import com.example.auth.screens.register.RegisterScreen
import com.example.navigation.graphs.auth.destinations.AuthDestinations


fun NavGraphBuilder.authGraph(
    navController: NavHostController
){

    navigation<AuthDestinations.AuthDestinationGraph>(
        startDestination = AuthDestinations.LogInScreenDestination,
    ){
       composable<AuthDestinations.LogInScreenDestination>{
           LogInScreen(
               onLoginSuccess = { role ->
                   /* Приходит роль пользователя,
                   можем навигировать пользователя на экран clent если пользователь выбрал клиента
                   а если пользователь выбрал менеджера то на экраны менеджеров

                   */
               },
               onNavigateToRegister = {
                   //Навигация на экран регистрации
               }
           )
       }

        composable<AuthDestinations.RegisterScreenDestination>{
            RegisterScreen(
                onRegisterSuccess = {role ->
                    //Тут приходит роль пользователя, можно что то сделать с ним
                },
                onNavigateToLogin = {
                    //Навигация на экран Login чтобы пользователь вошел в систему
                }
            )
        }
    }
}