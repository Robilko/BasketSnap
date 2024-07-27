package ru.robilko.basket_snap.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import ru.robilko.basket_snap.R
import ru.robilko.basket_snap.navigation.TopLevelDestination
import ru.robilko.favourites.navigation.FAVOURITES_ROUTE
import ru.robilko.favourites.navigation.navigateToFavouritesGraph
import ru.robilko.home.navigation.COUNTRIES_ROUTE
import ru.robilko.home.navigation.HOME_ROUTE
import ru.robilko.home.navigation.LEAGUES_ROUTE
import ru.robilko.home.navigation.navigateToHomeGraph
import ru.robilko.settings.navigation.SETTINGS_ROUTE
import ru.robilko.settings.navigation.navigateToSettingsGraph

@Composable
fun rememberBasketSnapAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): BasketSnapAppState {
    NavigationLoggingSideEffect(navController)
    return remember(coroutineScope, navController) {
        BasketSnapAppState(coroutineScope, navController)
    }
}

@Stable
class BasketSnapAppState(
    val coroutineScope: CoroutineScope,
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val topBarTitleId: Int?
        @Composable get() = when (currentDestination?.route) {
            HOME_ROUTE -> R.string.home_screen_title
            COUNTRIES_ROUTE -> R.string.countries_screen_title
            LEAGUES_ROUTE -> R.string.leagues_screen_title
            FAVOURITES_ROUTE -> R.string.favourites_screen_title
            SETTINGS_ROUTE -> R.string.settings_screen_title
            else -> null
        }

    val needToShowTopBar: Boolean
        @Composable get() = currentDestination?.route != HOME_ROUTE

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHomeGraph(navOptions)
            TopLevelDestination.FAVOURITES -> navController.navigateToFavouritesGraph(navOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettingsGraph(navOptions)
        }
    }
}

@Composable
private fun NavigationLoggingSideEffect(navController: NavHostController) {
    DisposableEffect(key1 = navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.i("Destination", destination.route.toString())
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
