package com.example.client.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PortfolioSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        // Заголовок секции
        Text(
            text = "Примеры работ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Контейнер для карточек с работами
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Первая работа

           /* закоментировал но потом придеться убрать комментарии

           PortfolioImage(
                modifier = Modifier.weight(1f),
                imageRes = R.drawable.work_table // Замени на свой ресурс
            )
            // Вторая работа
            PortfolioImage(
                modifier = Modifier.weight(1f),
                imageRes = R.drawable.work_door // Замени на свой ресурс
            )
            */
        }

        // Кнопка-ссылка "Смотреть все портфолио"
        TextButton(
            onClick = { /* Навигация в портфолио */ },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Смотреть все портфолио",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
fun PortfolioImage(modifier: Modifier, imageRes: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.aspectRatio(1f) // Делаем изображения квадратными
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Пример работы",
            contentScale = ContentScale.Crop, // Обрезаем по размеру, чтобы заполнить квадрат
            modifier = Modifier.fillMaxSize()
        )
    }
}