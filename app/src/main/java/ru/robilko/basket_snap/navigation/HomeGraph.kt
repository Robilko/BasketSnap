package ru.robilko.basket_snap.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import ru.robilko.games.navigation.gamesScreen
import ru.robilko.games.navigation.navigateToGames
import ru.robilko.home.navigation.HOME_ROUTE
import ru.robilko.home.navigation.homeScreen
import ru.robilko.league_details.navigation.leagueDetailsScreen
import ru.robilko.league_details.navigation.navigateToLeagueDetails
import ru.robilko.leagues.navigation.leaguesScreen
import ru.robilko.leagues.navigation.navigateToLeagues
import ru.robilko.team_details.navigation.navigateToTeamDetails
import ru.robilko.team_details.navigation.teamDetailsScreen
import ru.robilko.teams.navigation.navigateToTeams
import ru.robilko.teams.navigation.teamsScreen

const val HOME_GRAPH_ROUTE = "home_graph"

fun NavHostController.navigateToHomeGraph(navOptions: NavOptions? = null) {
    navigate(HOME_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navHostController: NavHostController,
    onTopBarTitleChange: (resId: Int) -> Unit
) {
    navigation(
        route = HOME_GRAPH_ROUTE,
        startDestination = HOME_ROUTE
    ) {
        homeScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagues = { navHostController.navigateToLeagues(it.id) })
        leaguesScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagueDetails = { navHostController.navigateToLeagueDetails(it.id) }
        )
        leagueDetailsScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeams = { leagueId, leagueName, season ->
                navHostController.navigateToTeams(
                    leagueId = leagueId,
                    leagueName = leagueName,
                    season = season
                )
            },
            onNavigateToGames = { leagueId, season ->
                navHostController.navigateToGames(leagueId = leagueId, season = season)
            }
        )
        teamsScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeamDetails = { teamInfo, season ->
                navHostController.navigateToTeamDetails(
                    teamId = teamInfo.id,
                    leagueId = teamInfo.leagueId,
                    season = season
                )
            }
        )
        teamDetailsScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagues = { countryId -> navHostController.navigateToLeagues(countryId) },
            onNavigateToLeagueDetails = { leagueId ->
                navHostController.navigateToLeagueDetails(leagueId)
            },
            onNavigateToAnotherTeamDetails = { teamId, leagueId, season ->
                navHostController.navigateToTeamDetails(teamId, leagueId, season)
            }
        )
        gamesScreen(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToTeamDetails = { teamId, leagueId, season ->
                navHostController.navigateToTeamDetails(
                    teamId = teamId,
                    leagueId = leagueId,
                    season = season
                )
            }
        )
    }
}