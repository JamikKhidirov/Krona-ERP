package com.example.client.screens.orderdetailscreen.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.client.screens.orderdetailscreen.OrderDetailScreen
import com.example.client.screens.orderdetailscreen.data.Document
import com.example.client.screens.orderdetailscreen.data.Order
import com.example.client.screens.orderdetailscreen.data.StatusUpdate
import com.example.client.screens.orderdetailscreen.repository.OrderDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun OrderDetailContent(
    order: Order,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Шапка с названием и статусом
        item { OrderHeader(order) }

        // Таймлайн статусов
        item { StatusTimeline(order.statusHistory) }

        // Фото изделия
        if (order.imageUrls.isNotEmpty()) {
            item { OrderPhotos(order.imageUrls) }
        }

        // Характеристики
        item { CharacteristicsSection(order) }

        // Документы
        if (order.documents.isNotEmpty()) {
            item { DocumentsSection(order.documents) }
        }

        // Мастер
        if (order.masterName.isNotEmpty()) {
            item { MasterCard(order) }
        }

        // Финансы
        item { FinanceSection(order) }

        // Комментарий
        if (order.comment.isNotEmpty()) {
            item { CommentSection(order.comment) }
        }

        // Нижний отступ
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}





// Добавь в конец файла OrderDetailScreen.kt

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
            createdAt = System.currentTimeMillis() - 86400000 * 17, // 17 дней назад
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
            comment = "Прошу учесть дополнительное освещение над рабочей зоной. Варочная панель индукционная, духовка с функцией пара."
        )

        // Используем remember для имитации StateFlow
        val orderFlow = remember { MutableStateFlow<Order?>(previewOrder) }
        val loadingFlow = remember { MutableStateFlow(false) }
        val errorFlow = remember { MutableStateFlow<String?>(null) }

        // Создаём фейковый ViewModel для Preview


        OrderDetailContent(
            order = previewOrder,
        )

}

