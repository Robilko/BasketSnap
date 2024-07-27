package ru.robilko.favourites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.robilko.favourites.presentation.FavouritesRoute

const val FAVOURITES_GRAPH_ROUTE = "favourites_graph"
const val FAVOURITES_ROUTE = "favourites"

fun NavHostController.navigateToFavouritesGraph(navOptions: NavOptions? = null) {
    navigate(FAVOURITES_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.favouritesGraph(navHostController: NavHostController) {
    navigation(
        route = FAVOURITES_GRAPH_ROUTE,
        startDestination = FAVOURITES_ROUTE
    ) {
        favouritesScreen()
    }
}

fun NavHostController.navigateToFavourites(navOptions: NavOptions? = null) {
    navigate(FAVOURITES_ROUTE, navOptions)
}

fun NavGraphBuilder.favouritesScreen() {
    composable(FAVOURITES_ROUTE) {
        FavouritesRoute()
    }
}