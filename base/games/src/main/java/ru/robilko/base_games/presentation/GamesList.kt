package ru.robilko.base_games.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.delay
import ru.robilko.base.util.HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN_2
import ru.robilko.base.util.HUMAN_DATE_PATTERN
import ru.robilko.base.util.toStringDate
import ru.robilko.base_games.R
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.EmptyList
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.ShimmerCard
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.model.data.GameResults

@Composable
fun GamesList(
    isLoading: Boolean,
    games: PersistentList<GameResults>,
    onClick: (GameResults) -> Unit,
    onTeamClick: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState(), enabled = false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            repeat(5) {
                ShimmerCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        val firstVisibleIndex = remember(games) {
            games.indexOfLast { it.statusLong != GAME_NOT_STARTED }.takeIf { it != -1 } ?: 0
        }
        val state =
            rememberLazyListState(initialFirstVisibleItemIndex = firstVisibleIndex)
        LazyColumn(
            state = state,
            modifier = modifier.testTag("GamesList"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (games.isEmpty()) {
                item {
                    EmptyList(
                        textResId = R.string.no_games_message,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    if (gameResults.isPlayingNow) FlashingDot()
                    AppText(
                        text = gameResults.date?.toStringDate(HUMAN_DATE_PATTERN).orEmpty(),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
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
fun FlashingDot() {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(800)
            visible = !visible
        }
    }

    Box(
        modifier = Modifier
            .size(10.dp)
            .background(if (visible) Color.Red else Color.Transparent, shape = CircleShape)
    )
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
                    maxWidth = 90.dp,
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