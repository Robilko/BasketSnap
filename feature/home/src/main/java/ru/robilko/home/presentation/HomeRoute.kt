package ru.robilko.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun HomeRoute(onNavigateToCountries: () -> Unit, modifier: Modifier = Modifier) {
    HomeScreen(modifier = modifier.fillMaxSize(), onNavigateToCountries = onNavigateToCountries)
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier, onNavigateToCountries: () -> Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Button(onClick = onNavigateToCountries) {
            Text(text = "Countries")
        }
    }
}