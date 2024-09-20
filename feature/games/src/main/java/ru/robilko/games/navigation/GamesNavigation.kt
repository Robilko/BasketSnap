package ru.robilko.games.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.games.presentation.GamesRoute

internal const val LEAGUE_ID_ARG = "leagueId"
internal const val SEASON_ARG = "season"
private const val GAMES_ROUTE_BASE = "games"
private const val GAMES_ROUTE = "$GAMES_ROUTE_BASE/{$LEAGUE_ID_ARG}/{$SEASON_ARG}"


fun NavHostController.navigateToGames(
    leagueId: Int,
    season: String,
    navOptions: NavOptions? = null
) {
    navigate("$GAMES_ROUTE_BASE/$leagueId/$season", navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit
) {
    composable(
        route = GAMES_ROUTE,
        arguments = listOf(
            navArgument(LEAGUE_ID_ARG) {
                nullable = false
                type = NavType.IntType
            },
            navArgument(SEASON_ARG) {
                nullable = false
                type = NavType.StringType
            }
        )
    ) {
        GamesRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeamDetails = onNavigateToTeamDetails
        )
    }
}