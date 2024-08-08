package ru.robilko.basket_snap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.robilko.basket_snap.ui.BasketSnapAppState

@Composable
internal fun AppNavHost(
    appState: BasketSnapAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onTopBarTitleChange: (resId: Int) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.HOME.route
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeGraph(navController, onTopBarTitleChange)
        favouritesGraph(navController, onTopBarTitleChange)
        settingsGraph(navController, onTopBarTitleChange)
    }
}