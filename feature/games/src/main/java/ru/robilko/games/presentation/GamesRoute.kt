package ru.robilko.games.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.robilko.base_games.presentation.GameDetailsDialog
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.base_games.presentation.GamesList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppSelectableOutlinedTextField
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.games.R

@Composable
internal fun GamesRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GamesViewModel = hiltViewModel<GamesViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.games_page_title) }
    GamesScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToTeamDetails = onNavigateToTeamDetails,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun GamesScreen(
    uiState: GamesUiState,
    onEvent: (GamesUiEvent) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier
) {
    uiState.detailsDialogState.let {
        if (it is GameDetailsDialogState.ShowData) {
            GameDetailsDialog(
                gameResults = it.gameResults,
                onDismiss = { onEvent(GamesUiEvent.DetailsDialogDismiss) }
            )
        }
    }

    Box(modifier = modifier) {
        when (uiState.dataState) {
            is DataState.Error -> {
                ErrorScreen(
                    text = uiState.dataState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetryButtonClick = uiState.dataState.onRetryAction
                )
            }

            else -> {
                AppSelectableOutlinedTextField(
                    title = stringResource(R.string.seasons_selectable_title),
                    selected = uiState.selectedSeason,
                    choices = uiState.seasons,
                    onSelectionChange = { onEvent(GamesUiEvent.SeasonClick(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                )
                GamesList(
                    isLoading = uiState.dataState is DataState.Loading,
                    games = uiState.gameResults,
                    onClick = { onEvent(GamesUiEvent.GameCardClick(it)) },
                    onTeamClick = onNavigateToTeamDetails,
                    modifier = Modifier
                        .padding(top = 100.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}