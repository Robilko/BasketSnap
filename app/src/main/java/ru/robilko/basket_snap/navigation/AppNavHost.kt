package ru.robilko.basket_snap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.robilko.basket_snap.ui.BasketSnapAppState
import ru.robilko.favourites.navigation.favouritesGraph
import ru.robilko.home.navigation.homeGraph
import ru.robilko.settings.navigation.settingsGraph

@Composable
internal fun AppNavHost(
    appState: BasketSnapAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.HOME.route
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeGraph(navController)
        favouritesGraph(navController)
        settingsGraph(navController)
    }
}