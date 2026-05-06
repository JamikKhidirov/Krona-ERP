package com.example.client.screens.myorders.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector





fun getProductTypeIcon(typeId: Int): ImageVector {
    return when (typeId) {
        1 -> Icons.Default.Home          // Стул
        2 -> Icons.Default.Build         // Стол
        3 -> Icons.Default.Star          // Кровать
        4 -> Icons.Default.Info          // Шкаф
        5 -> Icons.Default.ShoppingCart  // Кухня
        else -> Icons.Default.MoreVert    // Другое
    }
}
