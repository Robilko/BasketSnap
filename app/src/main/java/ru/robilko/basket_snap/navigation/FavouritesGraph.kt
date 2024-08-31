package ru.robilko.basket_snap.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import ru.robilko.favourites.navigation.FAVOURITES_ROUTE
import ru.robilko.favourites.navigation.favouritesScreen
import ru.robilko.league_details.navigation.leagueDetailsScreen
import ru.robilko.league_details.navigation.navigateToLeagueDetails
import ru.robilko.team_details.navigation.navigateToTeamDetails
import ru.robilko.team_details.navigation.teamDetailsScreen
import ru.robilko.teams.navigation.navigateToTeams
import ru.robilko.teams.navigation.teamsScreen

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
            onNavigateToLeagueDetails = { navHostController.navigateToLeagueDetails(it.id) },
            onNavigateToTeamDetails = navHostController::navigateToTeamDetails
        )
        leagueDetailsScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeams = { leagueId, season ->
                navHostController.navigateToTeams(leagueId = leagueId, season = season)
            }
        )
        teamsScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeamDetails = { teamInfo, season ->
                navHostController.navigateToTeamDetails(teamInfo = teamInfo, season = season)
            }
        )
        teamDetailsScreen(onTopBarTitleChange = onTopBarTitleChange)
    }
}