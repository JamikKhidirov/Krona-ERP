package com.example.client.screens.orderdetailscreen.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.client.screens.orderdetailscreen.core.getStatusConfig
import com.example.client.screens.orderdetailscreen.data.StatusUpdate

@Composable
fun TimelineItem(
    status: StatusUpdate,
    isLast: Boolean,
    isFirst: Boolean
) {
    val config = getStatusConfig(status.status)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Линия и точка
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(config.backgroundColor, CircleShape)
            )

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Текст
        Column(
            modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)
        ) {
            Text(
                text = config.label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (status.date.isNotEmpty()) {
                Text(
                    text = status.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (status.comment.isNotEmpty()) {
                Text(
                    text = status.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}




@Composable
@Preview(showBackground = true)
private fun PreviewTimeLineItem(){
    Column {
        TimelineItem(
            status = StatusUpdate(),
            isLast = true,
            isFirst = false
        )

        TimelineItem(
            status = StatusUpdate(),
            isLast = false,
            isFirst = true
        )

        TimelineItem(
            status = StatusUpdate(),
            isLast = true,
            isFirst = true
        )

        TimelineItem(
            status = StatusUpdate(),
            isLast = false,
            isFirst = false
        )
    }
}