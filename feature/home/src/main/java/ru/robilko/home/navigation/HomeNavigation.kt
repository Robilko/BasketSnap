package ru.robilko.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.robilko.home.presentation.HomeRoute
import ru.robilko.model.data.Country

const val HOME_ROUTE = "home"

fun NavHostController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagues: (Country) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit
) {
    composable(HOME_ROUTE) {
        HomeRoute(
            onTopBarTitleChange = onTopBarTitleChange,
            onNavigateToLeagues = onNavigateToLeagues,
            onNavigateToTeamDetails = onNavigateToTeamDetails
        )
    }
}