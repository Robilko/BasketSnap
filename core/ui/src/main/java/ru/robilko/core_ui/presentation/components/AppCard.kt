package ru.robilko.core_ui.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    border: BorderStroke? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border,
        content = {
            Box(modifier = Modifier.padding(contentPadding), content = content)
        }
    )
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    border: BorderStroke? = null,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border,
        onClick = onClick,
        content = {
            Box(modifier = Modifier.padding(contentPadding), content = content)
        }
    )
}