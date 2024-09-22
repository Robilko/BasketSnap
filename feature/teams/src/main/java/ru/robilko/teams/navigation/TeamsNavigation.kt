package ru.robilko.teams.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.model.data.TeamInfo
import ru.robilko.teams.presentation.TeamsRoute

internal const val LEAGUE_ID_ARG = "leagueId"
internal const val LEAGUE_NAME_ARG = "leagueName"
internal const val SEASON_ARG = "season"
private const val TEAMS_ROUTE_BASE = "teams"
private const val TEAMS_ROUTE =
    "$TEAMS_ROUTE_BASE/{$LEAGUE_ID_ARG}/{$LEAGUE_NAME_ARG}/{$SEASON_ARG}"


fun NavHostController.navigateToTeams(
    leagueId: Int,
    leagueName: String,
    season: String,
    navOptions: NavOptions? = null
) {
    navigate("$TEAMS_ROUTE_BASE/$leagueId/$leagueName/$season", navOptions)
}

fun NavGraphBuilder.teamsScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeamDetails: (TeamInfo, season: String?) -> Unit
) {
    composable(
        route = TEAMS_ROUTE,
        arguments = listOf(
            navArgument(LEAGUE_ID_ARG) {
                nullable = false
                type = NavType.IntType
            },
            navArgument(LEAGUE_NAME_ARG) {
                nullable = false
                type = NavType.StringType
            },
            navArgument(SEASON_ARG) {
                nullable = false
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val season = backStackEntry.arguments?.getString(SEASON_ARG)

        TeamsRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeamDetails = { onNavigateToTeamDetails(it, season) }
        )
    }
}