package com.example.manager.screens.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.manager.data.Order
import com.example.manager.data.OrderHistoryItem
import com.example.manager.data.OrderPriority
import com.example.manager.screens.orderdetail.viewmodel.ManagerOrderDetailViewModel
import com.example.manager.screens.orders.core.getStatusConfig
import com.example.manager.screens.orders.core.getStatusLabel
import com.example.manager.screens.orders.uikit.StatusChip
import com.example.network.data.OrderStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerOrderDetailScreen(
    orderId: String,
    onNavigateBack: () -> Unit,
    viewModel: ManagerOrderDetailViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsState()
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.successMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
        viewModel.observeHistory(orderId)
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }
    LaunchedEffect(success) {
        success?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Детали заказа") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6366F1),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading && order == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF6366F1)
                    )
                }
                error != null && order == null -> {
                    Box(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(error ?: "Ошибка загрузки", color = Color(0xFF64748B))
                        }
                    }
                }
                order != null -> {
                    OrderDetailContent(
                        order = order!!,
                        history = history,
                        currentManagerId = viewModel.currentManagerId,
                        isLoading = isLoading,
                        onAssign = { viewModel.assignOrder(orderId) },
                        onUpdateStatus = { status -> viewModel.updateStatus(orderId, status) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderDetailContent(
    order: Order,
    history: List<OrderHistoryItem>,
    currentManagerId: String,
    isLoading: Boolean,
    onAssign: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    val isMyOrder = order.managerId == currentManagerId
    val isUnassigned = order.managerId.isBlank() && order.status == OrderStatus.PENDING.name

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item { OrderDetailHeader(order) }

        // Photos
        if (order.imageUrls.isNotEmpty()) {
            item { OrderPhotosSection(order.imageUrls) }
        }

        // History Timeline
        item { OrderHistoryTimeline(history) }

        // Characteristics
        item { CharacteristicsSection(order) }

        // Financial Info
        item { FinanceSection(order) }

        // Client Info
        item { ClientInfoSection(order) }

        // Address
        if (order.address.isNotBlank()) {
            item { AddressSection(order) }
        }

        // Comment
        if (order.comment.isNotBlank()) {
            item { CommentSection(order.comment) }
        }

        // Manager Comment
        if (order.managerComment.isNotBlank()) {
            item { ManagerCommentSection(order.managerComment) }
        }

        // Action buttons
        item {
            OrderActionButtons(
                order = order,
                isMyOrder = isMyOrder,
                isUnassigned = isUnassigned,
                isLoading = isLoading,
                onAssign = onAssign,
                onUpdateStatus = onUpdateStatus
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun OrderDetailHeader(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${order.id.takeLast(6).uppercase()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF94A3B8)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (order.priority == OrderPriority.URGENT.name) {
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "СРОЧНО",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFEF4444)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    StatusChip(status = order.status)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = order.title.ifEmpty { order.productTypeName },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            if (order.productTypeName.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = order.productTypeName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B)
                )
            }

            if (order.createdAt > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(14.dp), tint = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Создан: ${formatTimestamp(order.createdAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            if (order.deadline.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(14.dp), tint = Color(0xFF6366F1))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Дедлайн: ${order.deadline}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6366F1),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderPhotosSection(imageUrls: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Фото товара",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(12.dp))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrls.firstOrNull())
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото товара",
                modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            if (imageUrls.size > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(imageUrls.drop(1).take(4)) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (imageUrls.size > 5) {
                        item {
                            Box(
                                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "+${imageUrls.size - 5}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryTimeline(history: List<OrderHistoryItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "История заказа",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (history.isEmpty()) {
                Text(
                    text = "История пока пуста",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            } else {
                history.forEachIndexed { index, item ->
                    TimelineHistoryItem(
                        item = item,
                        isLast = index == history.lastIndex,
                        isFirst = index == 0
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineHistoryItem(
    item: OrderHistoryItem,
    isLast: Boolean,
    isFirst: Boolean
) {
    val config = getStatusConfig(item.status)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            if (!isFirst) {
                Box(modifier = Modifier.width(2.dp).height(20.dp).background(Color(0xFFE2E8F0)))
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
            Box(modifier = Modifier.size(12.dp).background(config.backgroundColor, CircleShape))
            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).height(40.dp).background(Color(0xFFE2E8F0)))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
            Text(
                text = config.label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B)
            )
            if (item.timestamp > 0) {
                Text(
                    text = formatTimestamp(item.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (item.managerName.isNotBlank()) {
                Text(
                    text = item.managerName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (item.comment.isNotBlank()) {
                Text(
                    text = item.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CharacteristicsSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Характеристики",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (order.description.isNotBlank()) {
                DetailRow("Описание", order.description)
                Spacer(modifier = Modifier.height(8.dp))
            }
            DetailRow(Icons.Default.Straighten, "Размеры", "${order.widthCm}×${order.heightCm}×${order.depthCm} см")
            Spacer(modifier = Modifier.height(8.dp))
            if (order.material.isNotBlank()) {
                DetailRow(Icons.Default.Build, "Материал", order.material)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (order.color.isNotBlank()) {
                DetailRow("Цвет", order.color)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (order.facade.isNotBlank()) {
                DetailRow("Фасад", order.facade)
            }
            if (order.hardware.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Фурнитура", order.hardware)
            }
        }
    }
}

@Composable
private fun FinanceSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Финансы",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailRow(Icons.Default.AttachMoney, "Бюджет", "${order.budget} ₽")
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Оплачено", "${order.paidAmount} ₽")
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Остаток", order.getRemainingAmount())
            if (order.costPrice != "0") {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Себестоимость", "${order.costPrice} ₽")
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Прибыль", order.getCalculatedProfit())
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Маржа", "${order.getMarginPercent()}%")
            }
        }
    }
}

@Composable
private fun ClientInfoSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Клиент",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (order.clientName.isNotBlank()) {
                DetailRow(Icons.Default.Person, "Имя", order.clientName)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (order.clientPhone.isNotBlank()) {
                DetailRow(Icons.Default.Phone, "Телефон", order.clientPhone)
            }
        }
    }
}

@Composable
private fun AddressSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Адрес доставки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(Icons.Default.LocationOn, "Адрес", order.getFullAddress())
            if (order.deliveryType.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                val deliveryLabel = when (order.deliveryType) {
                    "SELF_PICKUP" -> "Самовывоз"
                    "DELIVERY" -> "Доставка"
                    "ASSEMBLY" -> "Доставка + Сборка"
                    else -> order.deliveryType
                }
                DetailRow("Тип доставки", deliveryLabel)
                if (order.deliveryCost != "0") {
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Стоимость доставки", "${order.deliveryCost} ₽")
                }
            }
        }
    }
}

@Composable
private fun CommentSection(comment: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Комментарий клиента",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF78350F)
            )
        }
    }
}

@Composable
private fun ManagerCommentSection(comment: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                text = "Комментарий менеджера",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E40AF)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1E3A5F)
            )
        }
    }
}

@Composable
private fun OrderActionButtons(
    order: Order,
    isMyOrder: Boolean,
    isUnassigned: Boolean,
    isLoading: Boolean,
    onAssign: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        when {
            isUnassigned -> {
                Button(
                    onClick = onAssign,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AddTask, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Взять в работу", fontWeight = FontWeight.Medium)
                }
            }

            isMyOrder -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showStatusMenu = true },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Изменить статус")
                        }

                        DropdownMenu(
                            expanded = showStatusMenu,
                            onDismissRequest = { showStatusMenu = false }
                        ) {
                            val availableStatuses = when (order.status) {
                                OrderStatus.ASSIGNED.name -> listOf(OrderStatus.IN_PROGRESS, OrderStatus.CANCELLED)
                                OrderStatus.IN_PROGRESS.name -> listOf(OrderStatus.READY, OrderStatus.CANCELLED)
                                OrderStatus.READY.name -> listOf(OrderStatus.DELIVERING, OrderStatus.COMPLETED)
                                OrderStatus.DELIVERING.name -> listOf(OrderStatus.COMPLETED)
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

                    if (order.clientPhone.isNotBlank()) {
                        IconButton(
                            onClick = { /* Intent на звонок */ },
                            modifier = Modifier.size(52.dp)
                        ) {
                            Icon(Icons.Default.Phone, "Позвонить", tint = Color(0xFF10B981))
                        }
                    }
                }
            }
        }

        // Кнопка связи с клиентом
        if (order.clientPhone.isNotBlank() && !isMyOrder && !isUnassigned) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /* Intent на звонок */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Phone, null, modifier = Modifier.size(18.dp), tint = Color(0xFF10B981))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Позвонить клиенту", color = Color(0xFF10B981))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            modifier = Modifier.weight(0.35f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B),
            modifier = Modifier.weight(0.65f)
        )
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF6366F1)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B),
            modifier = Modifier.weight(0.6f)
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
