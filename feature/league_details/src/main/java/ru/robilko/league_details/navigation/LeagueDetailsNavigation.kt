package ru.robilko.league_details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.league_details.presentation.LeagueDetailsRoute

internal const val LEAGUE_ID_ARG = "leagueId"
private const val LEAGUE_DETAILS_ROUTE_BASE = "league_details"
private const val LEAGUE_DETAILS_ROUTE = "$LEAGUE_DETAILS_ROUTE_BASE/{$LEAGUE_ID_ARG}"

fun NavHostController.navigateToLeagueDetails(leagueId: Int, navOptions: NavOptions? = null) {
    navigate("$LEAGUE_DETAILS_ROUTE_BASE/$leagueId", navOptions)
}

fun NavGraphBuilder.leagueDetailsScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeams: (leagueId: Int, season: String) -> Unit,
    onNavigateToGames: (leagueId: Int, season: String) -> Unit
) {
    composable(
        route = LEAGUE_DETAILS_ROUTE,
        arguments = listOf(navArgument(LEAGUE_ID_ARG) {
            nullable = false
            type = NavType.IntType
        }
        )
    ) {
        LeagueDetailsRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeams = onNavigateToTeams,
            onNavigateToGames = onNavigateToGames
        )
    }
}