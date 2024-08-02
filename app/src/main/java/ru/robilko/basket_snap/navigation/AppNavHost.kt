package ru.robilko.basket_snap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import ru.robilko.basket_snap.ui.BasketSnapAppState
import ru.robilko.favourites.navigation.favouritesGraph
import ru.robilko.home.navigation.HOME_ROUTE
import ru.robilko.home.navigation.homeScreen
import ru.robilko.leagues.navigation.leaguesScreen
import ru.robilko.leagues.navigation.navigateToLeagues
import ru.robilko.settings.navigation.settingsGraph

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

const val HOME_GRAPH_ROUTE = "home_graph"


fun NavHostController.navigateToHomeGraph(navOptions: NavOptions? = null) {
    navigate(HOME_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navHostController: NavHostController,
    onTopBarTitleChange: (resId: Int) -> Unit
) {
    navigation(
        route = HOME_GRAPH_ROUTE,
        startDestination = HOME_ROUTE
    ) {
        homeScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagues = { navHostController.navigateToLeagues(it.id) })
        leaguesScreen(onTopBarTitleChange)
    }
}