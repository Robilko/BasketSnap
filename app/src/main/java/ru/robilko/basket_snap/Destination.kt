package ru.robilko.basket_snap

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, @StringRes val titleResId: Int, val menuIcon: ImageVector? = null) {
    data object FirstScreen : Destination("first_screen", R.string.first_screen, Icons.Default.Home)
    data object SecondScreen : Destination("second_screen", R.string.second_screen, Icons.Default.Favorite);

    companion object {
        fun getBottomBarItems(): List<Destination> = listOf(
            FirstScreen, SecondScreen
        )
    }
}