package ru.robilko.basket_snap.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import ru.robilko.settings.navigation.SETTINGS_ROUTE
import ru.robilko.settings.navigation.settingsScreen

const val SETTINGS_GRAPH_ROUTE = "settings_graph"

fun NavHostController.navigateToSettingsGraph(navOptions: NavOptions? = null) {
    navigate(SETTINGS_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    onTopBarTitleChange: (resId: Int) -> Unit
) {
    navigation(
        route = SETTINGS_GRAPH_ROUTE,
        startDestination = SETTINGS_ROUTE
    ) {
        settingsScreen(onTopBarTitleChange)
    }
}