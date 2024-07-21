package ru.robilko.basket_snap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
internal fun AppNavHost(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
    onScreenChanged: (Destination) -> Unit
) {
    NavHost(navController = navHostController, startDestination = Destination.FirstScreen.route) {
        composable(Destination.FirstScreen.route) {
            Example(Destination.FirstScreen, paddingValues)
            LaunchedEffect(Unit) { onScreenChanged(Destination.FirstScreen) }
        }
        composable(Destination.SecondScreen.route) {
            Example(Destination.SecondScreen, paddingValues)
            LaunchedEffect(Unit) { onScreenChanged(Destination.SecondScreen) }
        }
    }
}

@Composable
fun Example(destination: Destination, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Text(text = stringResource(id = destination.titleResId))
    }
}