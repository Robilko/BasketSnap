package ru.robilko.league_details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.league_details.presentation.LeagueDetailsRoute

const val LEAGUE_ID_ARG = "leagueId"
const val LEAGUE_DETAILS_ROUTE_BASE = "league_details"
const val LEAGUE_DETAILS_ROUTE = "$LEAGUE_DETAILS_ROUTE_BASE/{$LEAGUE_ID_ARG}"

fun NavHostController.navigateToLeagueDetails(leagueId: Int, navOptions: NavOptions? = null) {
    navigate("$LEAGUE_DETAILS_ROUTE_BASE/$leagueId", navOptions)
}

fun NavGraphBuilder.leagueDetailsScreen(onTopBarTitleChange: (resId: Int) -> Unit) {
    composable(
        route = LEAGUE_DETAILS_ROUTE,
        arguments = listOf(navArgument(LEAGUE_ID_ARG) {
            nullable = false
            type = NavType.StringType
        }
        )
    ) {
        LeagueDetailsRoute(onTopBarTitleChange = onTopBarTitleChange)
    }
}