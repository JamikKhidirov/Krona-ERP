package com.example.uikit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun AuthText(
    text: String = "Логин",
    modifier: Modifier = Modifier,
){
    Text(
        text = text,
        modifier = modifier,
        fontSize =14.sp,
        color = Color(0xFF454650),
    )
}
