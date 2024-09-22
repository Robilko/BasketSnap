package ru.robilko.basket_snap.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import ru.robilko.favourites.navigation.FAVOURITES_ROUTE
import ru.robilko.favourites.navigation.favouritesScreen
import ru.robilko.games.navigation.gamesScreen
import ru.robilko.games.navigation.navigateToGames
import ru.robilko.league_details.navigation.leagueDetailsScreen
import ru.robilko.league_details.navigation.navigateToLeagueDetails
import ru.robilko.leagues.navigation.leaguesScreen
import ru.robilko.leagues.navigation.navigateToLeagues
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
            onNavigateToTeamDetails = { teamInfo ->
                navHostController.navigateToTeamDetails(
                    teamId = teamInfo.id,
                    leagueId = teamInfo.leagueId
                )
            }
        )
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