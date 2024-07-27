package ru.robilko.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SettingsRoute(modifier: Modifier = Modifier) {
    SettingsScreen(modifier)
}

@Composable
private fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Settings")
    }
}