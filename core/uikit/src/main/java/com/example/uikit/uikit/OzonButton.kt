package com.example.uikit.uikit

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
@Preview(showBackground = true)
fun OzonStyleButton() {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = ""
    )

    val transition = rememberInfiniteTransition(label = "")

    val animatedOffset by transition.animateFloat(
        initialValue = -300f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    Button(
        onClick = {},
        interactionSource = interactionSource,

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF005BFF)
        ),
        shape = RoundedCornerShape(13.dp),

        modifier = Modifier
            .graphicsLayer {

                scaleX = scale
                scaleY = scale
            }
            .drawWithContent {

                drawContent()

                if (isPressed) {

                    drawRect(

                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.4f),
                                Color.Transparent
                            ),

                            start = Offset(animatedOffset, 0f),

                            end = Offset(
                                animatedOffset + 200f,
                                size.height
                            )
                        )
                    )
                }
            }

    ) {

        Text(
            "Купить",
            color = Color.White,
            modifier = Modifier.padding(
                horizontal = 50.dp
            )
        )
    }
}