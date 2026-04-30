package com.example.client.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Preview(showBackground = true)
@Composable
fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.List, contentDescription = null, tint = Color.DarkGray)
            Spacer(Modifier.width(8.dp))
            Text("Крона", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Menu, contentDescription = null)
        }
        Spacer(Modifier.height(24.dp))
        Text("Новый заказ", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
        Text(
            "Опишите вашу идею, и мы воплотим её в дереве с безупречной точностью.",
            fontSize = 14.sp, color = Color.Gray
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductTypeGrid(
    selectedType: String = "",
    onTypeSelected: (String) -> Unit = {}
) {
    val items = listOf("Стул / Кресло", "Стол", "Кровать", "Шкаф", "Кухня", "Другое")

    Text(
        "ТИП ИЗДЕЛИЯ",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )
    Spacer(Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in items.indices step 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Первый элемент в строке
                ProductItem(
                    title = items[i],
                    isSelected = items[i] == selectedType,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTypeSelected(items[i]) } // Логика клика
                )
                // Второй элемент в строке
                ProductItem(
                    title = items[i+1],
                    isSelected = items[i+1] == selectedType,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTypeSelected(items[i+1]) } // Логика клика
                )
            }
        }
    }
}


@Composable
fun ProductItem(title: String, isSelected: Boolean, modifier: Modifier) {
    Surface(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF6366F1) else Color(0xFFE2E8F0)),
        color = if (isSelected) Color(0xFFF5F3FF) else Color.White,
        tonalElevation = if (isSelected) 2.dp else 0.dp // Легкое выделение
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            // Иконки можно менять в зависимости от названия, но для примера оставим Home
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF6366F1) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = if (isSelected) Color(0xFF6366F1) else Color.DarkGray
            )
        }
    }
}