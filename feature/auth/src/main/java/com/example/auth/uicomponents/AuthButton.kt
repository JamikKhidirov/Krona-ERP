package com.example.auth.uicomponents

import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
fun AuthButton(
    modifier: Modifier = Modifier,
    buttonState: AuthButtonState = AuthButtonState.REGISTER,
    onClickButton: () -> Unit = {}
){
    val state by remember(buttonState) {
        mutableStateOf(buttonState)
    }

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

    when (state) {
        AuthButtonState.LOGIN -> {
            Button(
                modifier = modifier.then(animatedModifier),
                onClick = onClickButton,
                shape = RoundedCornerShape(
                    12.dp
                ),
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25326A)
                )
            ) {
                Text(
                    text = "Войти",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        vertical = 5.dp,
                        horizontal = 40.dp)
                )
            }
        }
        AuthButtonState.REGISTER -> {
            OutlinedButton(
                modifier = modifier.then(animatedModifier),
                onClick = onClickButton,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFF25326A),
                ),
                interactionSource = interactionSource
            ) {
                Text(
                    text = "Регистрация",
                    modifier = Modifier.padding(
                        vertical = 5.dp,
                        horizontal = 40.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF25326A)
                )
            }
        }
    }
}


enum class AuthButtonState {
    LOGIN,
    REGISTER
}