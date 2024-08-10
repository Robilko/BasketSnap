package ru.robilko.basket_snap.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import ru.robilko.favourites.navigation.FAVOURITES_ROUTE
import ru.robilko.favourites.navigation.favouritesScreen
import ru.robilko.league_details.navigation.leagueDetailsScreen
import ru.robilko.league_details.navigation.navigateToLeagueDetails

const val FAVOURITES_GRAPH_ROUTE = "favourites_graph"

fun NavHostController.navigateToFavouritesGraph(navOptions: NavOptions? = null) {
    navigate(FAVOURITES_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.favouritesGraph(
    navHostController: NavHostController,
    onTopBarTitleChange: (resId: Int) -> Unit,
) {
    navigation(
        route = FAVOURITES_GRAPH_ROUTE,
        startDestination = FAVOURITES_ROUTE
    ) {
        favouritesScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagueDetails = { navHostController.navigateToLeagueDetails(it.id) }
        )
        leagueDetailsScreen(onTopBarTitleChange = onTopBarTitleChange)
    }
}