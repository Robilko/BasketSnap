package ru.robilko.team_details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.team_details.presentation.TeamDetailsRoute

internal const val TEAM_ID_ARG = "teamId"
internal const val LEAGUE_ID_ARG = "leagueId"
internal const val SEASON_ARG = "season"
private const val TEAM_DETAILS_ROUTE_BASE = "team_details"
private const val TEAM_DETAILS_ROUTE =
    "$TEAM_DETAILS_ROUTE_BASE/{$TEAM_ID_ARG}/{$LEAGUE_ID_ARG}/{$SEASON_ARG}"

fun NavHostController.navigateToTeamDetails(
    teamId: Int,
    leagueId: Int,
    season: String? = null,
    navOptions: NavOptions? = null
) {
    navigate("$TEAM_DETAILS_ROUTE_BASE/${teamId}/${leagueId}/$season", navOptions)
}

fun NavGraphBuilder.teamDetailsScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (leagueId: Int) -> Unit,
    onNavigateToLeagues: (countryId: Int) -> Unit,
    onNavigateToAnotherTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
) {
    composable(
        route = TEAM_DETAILS_ROUTE,
        arguments = listOf(navArgument(TEAM_ID_ARG) {
            nullable = false
            type = NavType.IntType
        },
            navArgument(LEAGUE_ID_ARG) {
                nullable = false
                type = NavType.IntType
            },
            navArgument(SEASON_ARG) {
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        TeamDetailsRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagues = onNavigateToLeagues,
            onNavigateToLeagueDetails = onNavigateToLeagueDetails,
            onNavigateToAnotherTeamDetails = onNavigateToAnotherTeamDetails
        )
    }
}