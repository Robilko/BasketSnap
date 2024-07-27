package ru.robilko.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.robilko.settings.presentation.SettingsRoute

const val SETTINGS_GRAPH_ROUTE = "settings_graph"
const val SETTINGS_ROUTE = "settings"

fun NavHostController.navigateToSettingsGraph(navOptions: NavOptions? = null) {
    navigate(SETTINGS_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        route = SETTINGS_GRAPH_ROUTE,
        startDestination = SETTINGS_ROUTE
    ) {
        settingsScreen()
    }
}

fun NavHostController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable(SETTINGS_ROUTE) {
        SettingsRoute()
    }
}