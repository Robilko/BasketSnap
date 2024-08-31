package ru.robilko.team_details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.model.data.TeamInfo
import ru.robilko.team_details.presentation.TeamDetailsRoute

const val TEAM_ID_ARG = "teamId"
const val LEAGUE_ID_ARG = "leagueId"
const val SEASON_ARG = "season"
const val TEAM_DETAILS_ROUTE_BASE = "team_details"
const val TEAM_DETAILS_ROUTE =
    "$TEAM_DETAILS_ROUTE_BASE/{$TEAM_ID_ARG}/{$LEAGUE_ID_ARG}/{$SEASON_ARG}"

fun NavHostController.navigateToTeamDetails(
    teamInfo: TeamInfo,
    season: String? = null,
    navOptions: NavOptions? = null
) {
    navigate("$TEAM_DETAILS_ROUTE_BASE/${teamInfo.id}/${teamInfo.leagueId}/$season", navOptions)
}

fun NavGraphBuilder.teamDetailsScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (leagueId: Int) -> Unit,
    onNavigateToLeagues: (countryId: Int) -> Unit,
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
            onNavigateToLeagueDetails = onNavigateToLeagueDetails
        )
    }
}