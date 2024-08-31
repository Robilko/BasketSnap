package ru.robilko.core_ui.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.robilko.core_ui.presentation.components.AppCard

@Composable
fun ShimmerAnimation(
    modifier: Modifier
) {
    val transition = rememberInfiniteTransition(label = "")
    val offsetValueStart by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 600, delayMillis = 600, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )
    val offsetValueEnd by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(0.7f),
            Color.LightGray.copy(0.5f),
            Color.LightGray.copy(0.2f),
            Color.LightGray.copy(0.5f),
            Color.LightGray.copy(0.7f)
        ),
        start = Offset(offsetValueStart, offsetValueStart),
        end = Offset(offsetValueEnd, offsetValueEnd)
    )

    Spacer(modifier = modifier.background(brush = brush))
}

@Composable
fun ShimmerCard(
    modifier: Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
) {
    AppCard(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(),
        shape = shape,
        border = border
    ) {
        ShimmerAnimation(modifier = modifier)
    }
}