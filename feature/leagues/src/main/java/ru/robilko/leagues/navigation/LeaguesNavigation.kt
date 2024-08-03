package ru.robilko.leagues.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.robilko.leagues.presentation.LeaguesRoute

const val COUNTRY_ID_ARG = "countryId"
const val LEAGUES_ROUTE_BASE = "leagues"
const val LEAGUES_ROUTE = "$LEAGUES_ROUTE_BASE/{$COUNTRY_ID_ARG}"


fun NavHostController.navigateToLeagues(countryId: Int, navOptions: NavOptions? = null) {
    navigate("$LEAGUES_ROUTE_BASE/$countryId", navOptions)
}

fun NavGraphBuilder.leaguesScreen(onTopBarTitleChange: (resId: Int) -> Unit) {
    composable(
        route = LEAGUES_ROUTE,
        arguments = listOf(navArgument(COUNTRY_ID_ARG) {
            nullable = false
            type = NavType.StringType
        }
        )
    ) {
        LeaguesRoute(onTopBarTitleChange)
    }
}