package com.example.client.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class FactoryAddress(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val phone: String,
    val workHours: String
)

val koronaFactories = listOf(
    FactoryAddress(
        id = 1,
        name = "Главный цех «Корона»",
        address = "ул. Индустриальная, д. 15",
        city = "Москва",
        phone = "+7 (495) 123-45-67",
        workHours = "Пн-Пт 9:00–20:00, Сб 10:00–18:00"
    ),
    FactoryAddress(
        id = 2,
        name = "Производство «Корона-Север»",
        address = "пр. Мебельщиков, д. 42",
        city = "Санкт-Петербург",
        phone = "+7 (812) 234-56-78",
        workHours = "Пн-Пт 9:00–19:00, Сб 10:00–17:00"
    ),
    FactoryAddress(
        id = 3,
        name = "Фабрика «Корона-Юг»",
        address = "ул. Заводская, д. 8",
        city = "Краснодар",
        phone = "+7 (861) 345-67-89",
        workHours = "Пн-Пт 9:00–18:00"
    ),
    FactoryAddress(
        id = 4,
        name = "Цех «Корона-Восток»",
        address = "ул. Промышленная, д. 21",
        city = "Екатеринбург",
        phone = "+7 (343) 456-78-90",
        workHours = "Пн-Пт 9:00–19:00"
    ),
    FactoryAddress(
        id = 5,
        name = "Мастерская «Корона-Запад»",
        address = "ул. Мастеров, д. 5",
        city = "Калининград",
        phone = "+7 (401) 567-89-01",
        workHours = "Пн-Пт 10:00–19:00"
    )
)

@Composable
fun DeliveryAddressSection(
    selectedAddress: FactoryAddress?,
    onAddressSelected: (FactoryAddress) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "МЕСТО ПОЛУЧЕНИЯ",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        Text(
            text = "Выберите цех фабрики «Корона», где удобно забрать заказ:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(koronaFactories) { factory ->
                val isSelected = selectedAddress?.id == factory.id

                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .clickable { onAddressSelected(factory) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFEEF2FF) else Color.White
                    ),
                    border = if (isSelected) BorderStroke(2.dp, Color(0xFF6366F1)) else BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF6366F1) else Color(0xFFF59E0B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = factory.city,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = factory.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = factory.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = factory.phone,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6366F1)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = factory.workHours,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        if (selectedAddress != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Заказ будет готов в: ${selectedAddress.name}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF166534)
                        )
                        Text(
                            text = "${selectedAddress.city}, ${selectedAddress.address}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF166534).copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
