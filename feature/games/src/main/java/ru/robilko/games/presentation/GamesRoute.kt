package ru.robilko.games.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import ru.robilko.base.util.HUMAN_DATE_PATTERN
import ru.robilko.base.util.toStringDate
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.EmptyList
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.games.R
import ru.robilko.model.data.GameResults

@Composable
internal fun GamesRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GamesViewModel = hiltViewModel<GamesViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.games_page_title) }
    GamesScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun GamesScreen(
    uiState: GamesUiState,
    onEvent: (GamesUiEvent) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        when (uiState.dataState) {
            DataState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            is DataState.Error -> {
                ErrorScreen(
                    text = uiState.dataState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetryButtonClick = uiState.dataState.onRetryAction
                )
            }

            DataState.Success -> {
                if (uiState.gameResults.isEmpty()) {
                    EmptyList(
                        textResId = R.string.no_games_message,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    GamesList(games = uiState.gameResults)
                }
            }
        }
    }
}

@Composable
private fun GamesList(games: PersistentList<GameResults>) {
    val firstVisibleIndex = remember(games) {
        games.indexOfLast { it.statusLong != "Not Started" }.takeIf { it != -1 } ?: 0
    }
    val state =
        rememberLazyListState(initialFirstVisibleItemIndex = firstVisibleIndex)
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games, key = { it.id }) { gameResults ->
            GameCard(gameResults = gameResults)
        }
    }
}

@Composable
private fun GameCard(gameResults: GameResults) {
    AppCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    text = gameResults.date?.toStringDate(HUMAN_DATE_PATTERN).orEmpty(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                AppText(
                    text = gameResults.time,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Team(
                    name = gameResults.homeTeam.name,
                    logoUrl = gameResults.homeTeam.logoUrl
                )
                ScoreBox(
                    total = "${gameResults.homeScore.total ?: "-"} : ${gameResults.awayScore.total ?: "-"}",
                    status = gameResults.statusLong
                )

                Team(
                    name = gameResults.awayTeam.name,
                    logoUrl = gameResults.awayTeam.logoUrl
                )
            }
        }
    }
}

@Composable
private fun RowScope.Team(name: String, logoUrl: String) {
    Column(
        modifier = Modifier.weight(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(logoUrl)
                .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        AppText(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RowScope.ScoreBox(total: String, status: String) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppText(text = stringResource(R.string.total_score), fontSize = 12.sp)
        AppText(
            text = total,
            fontSize = 30.sp
        )
        AppText(text = status, fontSize = 12.sp)
    }
}