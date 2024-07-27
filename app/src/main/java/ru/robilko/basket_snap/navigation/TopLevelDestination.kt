package ru.robilko.basket_snap.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import ru.robilko.basket_snap.R
import ru.robilko.favourites.navigation.FAVOURITES_GRAPH_ROUTE
import ru.robilko.home.navigation.HOME_GRAPH_ROUTE
import ru.robilko.settings.navigation.SETTINGS_GRAPH_ROUTE

enum class TopLevelDestination(
    val route: String,
    @StringRes val titleResId: Int,
    val menuIcon: ImageVector
) {
    HOME(
        route = HOME_GRAPH_ROUTE,
        titleResId = R.string.home_screen_title,
        menuIcon = Icons.Default.Home
    ),
    FAVOURITES(
        route = FAVOURITES_GRAPH_ROUTE,
        titleResId = R.string.favourites_screen_title,
        menuIcon = Icons.Default.Favorite
    ),
    SETTINGS(
        route = SETTINGS_GRAPH_ROUTE,
        titleResId = R.string.settings_screen_title,
        menuIcon = Icons.Default.Settings
    )
}