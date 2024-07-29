package ru.robilko.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.robilko.home.presentation.CountriesRoute
import ru.robilko.home.presentation.home.HomeRoute
import ru.robilko.home.presentation.LeaguesRoute

const val HOME_GRAPH_ROUTE = "home_graph"
const val HOME_ROUTE = "home"
const val COUNTRIES_ROUTE = "countries"
const val LEAGUES_ROUTE = "leagues"


fun NavHostController.navigateToHomeGraph(navOptions: NavOptions? = null) {
    navigate(HOME_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.homeGraph(navHostController: NavHostController) {
    navigation(
        route = HOME_GRAPH_ROUTE,
        startDestination = HOME_ROUTE
    ) {
        homeScreen(onNavigateToCountries = navHostController::navigateToCountries)
        countriesScreen(onNavigateToLeagues = navHostController::navigateToLeagues)
        leaguesScreen()
    }
}

fun NavHostController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(onNavigateToCountries: () -> Unit) {
    composable(HOME_ROUTE) {
        HomeRoute(onNavigateToCountries = onNavigateToCountries)
    }
}

fun NavHostController.navigateToCountries(navOptions: NavOptions? = null) {
    navigate(COUNTRIES_ROUTE, navOptions)
}


fun NavGraphBuilder.countriesScreen(onNavigateToLeagues: () -> Unit) {
    composable(COUNTRIES_ROUTE) {
        CountriesRoute(onNavigateToLeagues = onNavigateToLeagues)
    }
}

fun NavHostController.navigateToLeagues(navOptions: NavOptions? = null) {
    navigate(LEAGUES_ROUTE, navOptions)
}


fun NavGraphBuilder.leaguesScreen() {
    composable(LEAGUES_ROUTE) {
        LeaguesRoute()
    }
}