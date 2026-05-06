package com.example.client.screens.myorders.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.client.screens.myorders.core.getStatusInfo
import com.example.network.data.OrderStatus


@Composable
fun StatusChip(status: String) {
    val info = getStatusInfo(status)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = info.backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(info.color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = info.label,
                style = MaterialTheme.typography.labelSmall,
                color = info.color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}