package com.example.client.screens.orderdetailscreen.uikit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.client.screens.orderdetailscreen.data.Document
import com.example.client.screens.orderdetailscreen.data.Order
import com.example.client.screens.orderdetailscreen.data.StatusUpdate
import com.example.client.screens.orderdetailscreen.viewmodel.OrderDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun OrderDetailContent(
    order: Order,
    orderId: String,
    viewModel: OrderDetailViewModel,
    currentUserId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { OrderHeader(order) }

        item { StatusTimeline(order.statusHistory) }

        if (order.imageUrls.isNotEmpty()) {
            item { OrderPhotos(order.imageUrls) }
        }

        item { CharacteristicsSection(order) }

        if (order.documents.isNotEmpty()) {
            item { DocumentsSection(order.documents) }
        }

        if (order.masterName.isNotEmpty()) {
            item { MasterCard(order) }
        }

        item { FinanceSection(order) }

        if (order.comment.isNotEmpty()) {
            item { CommentSection(order.comment) }
        }

        // Кнопки связи с менеджером
        if (order.managerId.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Связь с менеджером",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.managerPhone}"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                enabled = order.managerPhone.isNotBlank()
                            ) {
                                Icon(Icons.Default.Phone, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Позвонить")
                            }
                        }
                    }
                }
            }
        }

        // Отзыв о заказе
        if (order.status in listOf("COMPLETED", "PAID")) {
            item {
                ReviewSection(
                    orderId = orderId,
                    currentRating = order.clientRating,
                    currentReview = order.clientReview,
                    onSaveReview = { id, rating, review ->
                        viewModel.saveReview(id, rating, review)
                    }
                )
            }
        }

        // Чат с менеджером
        if (order.managerId.isNotBlank()) {
            item {
                ChatSection(
                    orderId = orderId,
                    currentUserId = currentUserId,
                    viewModel = viewModel
                )
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}


@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun OrderDetailScreenPreview() {

        val previewOrder = Order(
            id = "preview-123",
            userId = "user-1",
            productTypeId = 5,
            productTypeName = "Кухня",
            title = "Кухонный гарнитур «Арктика»",
            description = "Кухня в скандинавском стиле с фасадами из МДФ эмали. Требуется встроенная техника и столешница из искусственного камня.",
            budget = "450 000",
            widthCm = "320",
            heightCm = "220",
            depthCm = "60",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800",
                "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800"
            ),
            status = "IN_PROGRESS",
            statusHistory = listOf(
                StatusUpdate("PENDING", "15.10.2023", "Заказ создан"),
                StatusUpdate("ACCEPTED", "16.10.2023", "Принят в работу"),
                StatusUpdate("MATERIALS", "18.10.2023", "Расход материалов"),
                StatusUpdate("ASSEMBLY", "25.10.2023", "Сборка модулей")
            ),
            createdAt = System.currentTimeMillis() - 86400000 * 17,
            deadline = "04.11.2023",
            material = "МДФ Эмаль Мат",
            color = "Белый (матовый)",
            facade = "Классический п-образный",
            masterId = "master-1",
            masterName = "Елена Волкова",
            masterPhone = "+7 (900) 123-45-67",
            masterPhotoUrl = "",
            masterRating = 4.8,
            documents = listOf(
                Document("Договор_2489_А.pdf", "https://example.com/doc1.pdf", "1.2 МБ"),
                Document("Схема_обмер.pdf", "https://example.com/doc2.pdf", "3.4 МБ")
            ),
            paidAmount = "225 000",
            comment = "Прошу учесть дополнительное освещение над рабочей зоной."
        )

        OrderDetailContent(
            order = previewOrder,
            orderId = "preview-123",
            viewModel = TODO(),
            currentUserId = "user-1"
        )

}
