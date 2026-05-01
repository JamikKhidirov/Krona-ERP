package com.example.client.screens.orderdetailscreen.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.client.screens.orderdetailscreen.data.Order


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
