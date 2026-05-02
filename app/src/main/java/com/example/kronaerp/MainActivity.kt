package com.example.kronaerp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kronaerp.ui.theme.KronaERPTheme
import com.example.manager.screens.orders.ManagerOrdersScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KronaERPTheme {
                ManagerOrdersScreen{

                }
            }
        }
    }
}

