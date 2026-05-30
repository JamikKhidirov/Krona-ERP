package com.example.manager.screens.statistics.uikit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.manager.screens.statistics.viewmodel.ManagerStatisticsViewModel
import com.example.manager.screens.statistics.viewmodel.MonthlyRevenue
import com.example.manager.screens.statistics.viewmodel.StatisticsState
import com.example.manager.uikit.bottombar.ManagerBottomNavigation
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerStatisticsScreen(
    navController: NavHostController,
    viewModel: ManagerStatisticsViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary,
                    navigationIconContentColor = colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            ManagerBottomNavigation(navController = navController)
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item { KeyMetricsSection(state) }
                item { SectionTitle("Статусы заказов") }
                item { StatusDistributionChart(state) }
                item { SectionTitle("Ежемесячная выручка") }
                item { MonthlyRevenueChart(state.monthlyRevenue) }
                item { SectionTitle("Приоритеты") }
                item { PriorityDistribution(state) }
                item { SectionTitle("Статусы оплаты") }
                item { PaymentStatusSection(state) }
                item { SectionTitle("Финансовая сводка") }
                item { FinancialSummary(state) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    val colorScheme = MaterialTheme.colorScheme
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = colorScheme.onSurface
    )
}

@Composable
private fun KeyMetricsSection(state: StatisticsState) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Общая выручка",
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatPrice(state.totalRevenue),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimaryContainer
            )
        }
    }

    Spacer(modifier = Modifier.height(0.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Всего заказов",
            value = "${state.totalOrders}",
            icon = Icons.Default.ShoppingCart,
            color = colorScheme.primary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Выполнено",
            value = "${state.statusStats.find { it.status == "COMPLETED" }?.count ?: 0}",
            icon = Icons.Default.CheckCircle,
            color = colorScheme.tertiary
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Прибыль",
            value = formatPrice(state.totalProfit),
            icon = Icons.Default.TrendingUp,
            color = colorScheme.tertiary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Маржа",
            value = "${String.format("%.1f", state.averageMargin)}%",
            icon = Icons.Default.Analytics,
            color = colorScheme.tertiary
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Средний чек",
            value = formatPrice(state.averageOrderValue),
            icon = Icons.Default.Receipt,
            color = colorScheme.tertiary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = "Просрочено",
            value = "${state.overdueCount}",
            icon = Icons.Default.Warning,
            color = if (state.overdueCount > 0) colorScheme.error else colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatusDistributionChart(state: StatisticsState) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            state.statusStats.filter { it.count > 0 }.forEach { stat ->
                StatusBar(
                    label = stat.label,
                    count = stat.count,
                    percentage = stat.percentage,
                    color = Color(stat.color)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun StatusBar(
    label: String,
    count: Int,
    percentage: Float,
    color: Color
) {
    val colorScheme = MaterialTheme.colorScheme
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface
            )
            Text(
                text = "$count (${String.format("%.1f", percentage)}%)",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = percentage / 100f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun MonthlyRevenueChart(monthlyData: List<MonthlyRevenue>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val colorScheme = MaterialTheme.colorScheme
            val maxRevenue = monthlyData.maxOfOrNull { it.revenue } ?: 1.0
            val barCount = monthlyData.size

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val barWidth = size.width / (barCount * 2 + 1)
                val chartHeight = size.height - 20f

                monthlyData.forEachIndexed { index, data ->
                    val barHeight = if (maxRevenue > 0) {
                        (data.revenue / maxRevenue * chartHeight).toFloat()
                    } else 0f

                    val x = barWidth * (index * 2 + 1)
                    val y = chartHeight - barHeight

                    drawRoundRect(
                        color = colorScheme.primary,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                    )

                    drawLine(
                        color = colorScheme.primary.copy(alpha = 0.3f),
                        start = Offset(0f, chartHeight),
                        end = Offset(size.width, chartHeight),
                        strokeWidth = 1f
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                monthlyData.forEachIndexed { index, data ->
                    Text(
                        text = data.month,
                        fontSize = 10.sp,
                        color = colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Всего за 12 мес: ${formatPrice(monthlyData.sumOf { it.revenue })}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = "${monthlyData.sumOf { it.count }} заказов",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PriorityDistribution(state: StatisticsState) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            state.priorityStats.forEach { stat ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(stat.color).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${stat.count}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(stat.color)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stat.label,
                        fontSize = 11.sp,
                        color = colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusSection(state: StatisticsState) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            state.paymentStats.forEach { stat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stat.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "${stat.count} · ${formatPrice(stat.total)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                }
                HorizontalDivider(
                    color = colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun FinancialSummary(state: StatisticsState) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FinancialRow("Выручка", formatPrice(state.totalRevenue), colorScheme.tertiary)
            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.5f))
            FinancialRow("Прибыль", formatPrice(state.totalProfit), colorScheme.primary)
            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.5f))
            FinancialRow("Средняя маржа", "${String.format("%.1f", state.averageMargin)}%", colorScheme.tertiary)
            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.5f))
            FinancialRow("Средний чек", formatPrice(state.averageOrderValue), colorScheme.tertiary)
        }
    }
}

@Composable
private fun FinancialRow(label: String, value: String, valueColor: Color) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

private fun formatPrice(amount: Double): String {
    val nf = NumberFormat.getNumberInstance(Locale("ru"))
    nf.maximumFractionDigits = 0
    return "${nf.format(amount.toLong())} ₽"
}
