package ru.robilko.favourites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.robilko.favourites.presentation.FavouritesRoute
import ru.robilko.model.data.League

const val FAVOURITES_ROUTE = "favourites"

fun NavHostController.navigateToFavourites(navOptions: NavOptions? = null) {
    navigate(FAVOURITES_ROUTE, navOptions)
}

fun NavGraphBuilder.favouritesScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit
) {
    composable(FAVOURITES_ROUTE) {
        FavouritesRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagueDetails = onNavigateToLeagueDetails
        )
    }
}