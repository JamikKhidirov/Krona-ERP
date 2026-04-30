package com.example.auth.screens.register.uicomponents

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true)
fun ButtonAuthNavLogIn(
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit = {}
){

    val interactionSource = remember { MutableInteractionSource() }
    // 2. Отслеживаем, нажата ли кнопка
    val isPressed by interactionSource.collectIsPressedAsState()

    // 3. Анимируем масштаб (scale). Если нажата — 0.95f, если нет — 1f
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scaleAnimation"
    )

    val animatedModifier = modifier
        .graphicsLayer(scaleX = scale, scaleY = scale)

    TextButton(
        onClick = onClickButton,
        modifier = modifier.then(animatedModifier),
        interactionSource = interactionSource,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp)
        ) {
            Text(
                "Уже есть аккаунт? ",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text("Войти",
                fontSize = 14.sp,
                color = Color(0xFF25326A),
                fontWeight = FontWeight.Bold
            )

        }
    }
}