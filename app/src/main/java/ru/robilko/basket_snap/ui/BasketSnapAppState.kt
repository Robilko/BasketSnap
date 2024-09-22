package ru.robilko.basket_snap.ui

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.robilko.basket_snap.navigation.FAVOURITES_GRAPH_ROUTE
import ru.robilko.basket_snap.navigation.HOME_GRAPH_ROUTE
import ru.robilko.basket_snap.navigation.SETTINGS_GRAPH_ROUTE
import ru.robilko.basket_snap.navigation.TopLevelDestination
import ru.robilko.basket_snap.navigation.navigateToFavouritesGraph
import ru.robilko.basket_snap.navigation.navigateToHomeGraph
import ru.robilko.basket_snap.navigation.navigateToSettingsGraph
import ru.robilko.home.navigation.HOME_ROUTE
import ru.robilko.settings.domain.model.AppConfigData
import ru.robilko.settings.domain.model.DarkThemeConfig
import ru.robilko.settings.domain.repo.AppConfigRepository

@Composable
fun rememberBasketSnapAppState(
    appConfigRepository: AppConfigRepository,
    navController: NavHostController = rememberNavController()
): BasketSnapAppState {
    NavigationLoggingSideEffect(navController)
    return remember(appConfigRepository, navController) {
        BasketSnapAppState(appConfigRepository, navController)
    }
}

@Stable
class BasketSnapAppState(
    private val appConfigRepository: AppConfigRepository,
    val navController: NavHostController
) {
    private val appConfigData: AppConfigData
        @Composable get() = appConfigRepository.getSettingsData().collectAsState().value

    val isDarkTheme: Boolean
        @Composable get() = shouldUseDarkTheme(darkThemeConfig = appConfigData.darkThemeConfig)

    val needToShowTopBar: Boolean
        @Composable get() = appConfigData.needToShowTopBar

    private val navBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val needToShowTopBarBackButton: Boolean
        @Composable get() = navBackStackEntry?.destination?.route != HOME_ROUTE

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (navBackStackEntry?.destination?.parent?.route) {
            HOME_GRAPH_ROUTE -> TopLevelDestination.HOME
            FAVOURITES_GRAPH_ROUTE -> TopLevelDestination.FAVOURITES
            SETTINGS_GRAPH_ROUTE -> TopLevelDestination.SETTINGS
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination, needRestoreState: Boolean) {
        Log.d("TAG", "navigateToTopLevelDestination: $topLevelDestination")
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = needRestoreState
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

@Composable
@ReadOnlyComposable
private fun shouldUseDarkTheme(
    darkThemeConfig: DarkThemeConfig,
): Boolean = when (darkThemeConfig) {
    DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    DarkThemeConfig.LIGHT -> false
    DarkThemeConfig.DARK -> true
}
