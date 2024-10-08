package ru.robilko.leagues.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.leagues.presentation.LeaguesRoute
import ru.robilko.model.data.League

internal const val COUNTRY_ID_ARG = "countryId"
private const val LEAGUES_ROUTE_BASE = "leagues"
private const val LEAGUES_ROUTE = "$LEAGUES_ROUTE_BASE/{$COUNTRY_ID_ARG}"


fun NavHostController.navigateToLeagues(countryId: Int, navOptions: NavOptions? = null) {
    navigate("$LEAGUES_ROUTE_BASE/$countryId", navOptions)
}

fun NavGraphBuilder.leaguesScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit
) {
    composable(
        route = LEAGUES_ROUTE,
        arguments = listOf(navArgument(COUNTRY_ID_ARG) {
            nullable = false
            type = NavType.IntType
        }
        )
    ) {
        LeaguesRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagueDetails = onNavigateToLeagueDetails
        )
    }
}