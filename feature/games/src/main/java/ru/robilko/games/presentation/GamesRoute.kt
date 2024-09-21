package ru.robilko.games.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import ru.robilko.base.util.HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN_2
import ru.robilko.base.util.HUMAN_DATE_PATTERN
import ru.robilko.base.util.toStringDate
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppSelectableOutlinedTextField
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.EmptyList
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.games.R
import ru.robilko.model.data.GameResults

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
                onCountryClick = {},
                onLeagueClick = {},
                onDismiss = { onEvent(GamesUiEvent.DetailsDialogDismiss) }
            )
        }
    }

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
}

@Composable
private fun GamesList(
    games: PersistentList<GameResults>,
    onClick: (GameResults) -> Unit,
    onTeamClick: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstVisibleIndex = remember(games) {
        games.indexOfLast { it.statusLong != "Not Started" }.takeIf { it != -1 } ?: 0
    }
    val state =
        rememberLazyListState(initialFirstVisibleItemIndex = firstVisibleIndex)
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games, key = { it.id }) { gameResults ->
            GameCard(
                gameResults = gameResults,
                onClick = { onClick(gameResults) },
                onTeamClick = { teamId ->
                    onTeamClick(
                        teamId,
                        gameResults.league.id,
                        gameResults.league.season
                    )
                }
            )
        }
    }
}

@Composable
private fun GameCard(gameResults: GameResults, onClick: () -> Unit, onTeamClick: (Int) -> Unit) {
    AppCard(onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            if (gameResults.statusLong != GAME_NOT_STARTED) {
                AppText(
                    text = gameResults.date?.toStringDate(HUMAN_DATE_PATTERN).orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Team(
                    name = gameResults.homeTeam.name,
                    logoUrl = gameResults.homeTeam.logoUrl,
                    onClick = { onTeamClick(gameResults.homeTeam.id) }
                )
                if (gameResults.statusLong == GAME_NOT_STARTED) {
                    GameDayInfo(
                        date = gameResults.date?.toStringDate(HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN_2)
                            .orEmpty(),
                        venue = gameResults.venue
                    )
                } else {
                    ScoreBox(
                        total = "${gameResults.homeScore.total ?: "-"} : ${gameResults.awayScore.total ?: "-"}",
                        status = gameResults.statusLong
                    )
                }

                Team(
                    name = gameResults.awayTeam.name,
                    logoUrl = gameResults.awayTeam.logoUrl,
                    onClick = { onTeamClick(gameResults.awayTeam.id) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.Team(name: String, logoUrl: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(logoUrl)
                .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .requiredSizeIn(
                    minWidth = 80.dp,
                    minHeight = 80.dp,
                    maxWidth = 100.dp,
                    maxHeight = 80.dp
                )
                .bounceClick { onClick() },
            contentScale = ContentScale.Fit
        )
        AppText(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun RowScope.GameDayInfo(date: String, venue: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppText(
            text = date,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        AppText(
            text = venue,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RowScope.ScoreBox(total: String, status: String) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppText(text = stringResource(R.string.total_score), fontSize = 12.sp)
        AppText(text = total, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.heightIn(16.dp))
        AppText(
            text = status, fontSize = 13.sp, color = BasketSnapTheme.colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private const val GAME_NOT_STARTED = "Not Started"