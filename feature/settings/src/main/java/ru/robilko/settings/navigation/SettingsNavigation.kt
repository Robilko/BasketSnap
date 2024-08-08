package ru.robilko.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.robilko.settings.presentation.SettingsRoute

const val SETTINGS_ROUTE = "settings"

fun NavHostController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(onTopBarTitleChange: (resId: Int) -> Unit,) {
    composable(SETTINGS_ROUTE) {
        SettingsRoute(onTopBarTitleChange)
    }
}