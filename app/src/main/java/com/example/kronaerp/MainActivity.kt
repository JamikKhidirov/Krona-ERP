package com.example.kronaerp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize

import com.example.auth.screens.register.RegisterScreen
import com.example.client.screens.myorders.MyOrdersScreen
import com.example.client.screens.neworder.NewOrderScreen
import com.example.client.screens.orderdetailscreen.OrderDetailScreen
import com.example.kronaerp.ui.theme.KronaERPTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KronaERPTheme {
                RegisterScreen(
                    onNavigateToLogin = {},
                    onRegisterSuccess = {

                    }
                )
            }
        }
    }
}

