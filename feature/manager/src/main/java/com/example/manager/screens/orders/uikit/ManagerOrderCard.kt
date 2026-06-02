package com.example.manager.screens.orders.uikit


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.manager.data.Order
import com.example.manager.data.OrderPriority
import com.example.manager.screens.orders.core.getStatusConfig
import com.example.manager.screens.orders.core.getStatusLabel
import com.example.network.data.OrderStatus

@Composable
fun ManagerOrderCard(
    order: Order,
    currentManagerId: String,
    onClick: () -> Unit,
    onAssign: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    val statusConfig = getStatusConfig(order.status)
    val isMyOrder = order.managerId == currentManagerId
    val isUnassigned = order.managerId.isBlank() && order.status == OrderStatus.PENDING.name
    var showStatusMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Фото товара (если есть)
            if (order.imageUrls.isNotEmpty()) {
                val url = order.imageUrls.firstOrNull()
                val bitmap = remember(url) {
                    if (url != null) {
                        try {
                            val raw = url.substringAfter("base64,")
                            val bytes = android.util.Base64.decode(raw, android.util.Base64.DEFAULT)
                            android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                        } catch (_: Exception) { null }
                    } else null
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Фото товара",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Верхняя строка: ID, статус, приоритет
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${order.id.takeLast(4).uppercase()}",
                        style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Приоритет
                        if (order.priority == OrderPriority.URGENT.name) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "СРОЧНО",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Статус
                        StatusChip(status = order.status)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Название
                Text(
                    text = order.title.ifEmpty { order.productTypeName },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Краткое описание
                if (order.productTypeName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.productTypeName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Клиент
                if (order.clientName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(
                        icon = Icons.Default.Person,
                        text = "${order.clientName} · ${order.clientPhone}"
                    )
                }

                // Бюджет и материал
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    InfoRow(icon = Icons.Default.AttachMoney, text = "${order.budget} ₽")
                    Spacer(modifier = Modifier.width(16.dp))
                    if (order.material.isNotBlank()) {
                        InfoRow(icon = Icons.Default.Build, text = order.material)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))

                // === КНОПКИ ДЕЙСТВИЙ ===
                when {
                    // Заказ свободен — кнопка "Взять в работу"
                    isUnassigned -> {
                        Button(
                            onClick = onAssign,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.AddTask, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Взять в работу")
                        }
                    }

                    // Мой заказ — кнопки управления статусом
                    isMyOrder -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Кнопка смены статуса
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedButton(
                                    onClick = { showStatusMenu = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Изменить статус")
                                }

                                DropdownMenu(
                                    expanded = showStatusMenu,
                                    onDismissRequest = { showStatusMenu = false }
                                ) {
                                    val availableStatuses = when (order.status) {
                                        OrderStatus.ASSIGNED.name -> listOf(
                                            OrderStatus.IN_PROGRESS,
                                            OrderStatus.CANCELLED
                                        )

                                        OrderStatus.IN_PROGRESS.name -> listOf(
                                            OrderStatus.READY,
                                            OrderStatus.CANCELLED
                                        )

                                        OrderStatus.READY.name -> listOf(
                                            OrderStatus.DELIVERING,
                                            OrderStatus.COMPLETED
                                        )

                                        OrderStatus.DELIVERING.name -> listOf(
                                            OrderStatus.COMPLETED
                                        )

                                        OrderStatus.COMPLETED.name -> listOf(
                                            OrderStatus.PAID
                                        )

                                        OrderStatus.PAID.name -> emptyList()

                                        else -> emptyList()
                                    }

                                    availableStatuses.forEach { status ->
                                        DropdownMenuItem(
                                            text = { Text(getStatusLabel(status)) },
                                            onClick = {
                                                onUpdateStatus(status)
                                                showStatusMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Кнопка звонка клиенту
                            if (order.clientPhone.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:${order.clientPhone}"))
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Phone,
                                        "Позвонить",
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }

                    // Заказ другого менеджера
                    else -> {
                        Text(
                            "Менеджер: ${order.managerId.take(4)}...",
                            style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
