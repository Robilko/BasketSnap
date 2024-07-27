package ru.robilko.favourites.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun FavouritesRoute(modifier: Modifier = Modifier) {
    FavouritesScreen(modifier = modifier)
}

@Composable
private fun FavouritesScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Favourites")
    }
}