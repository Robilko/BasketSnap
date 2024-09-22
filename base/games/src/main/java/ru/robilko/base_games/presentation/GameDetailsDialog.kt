package ru.robilko.base_games.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import ru.robilko.base.util.HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN
import ru.robilko.base.util.toStringDate
import ru.robilko.base_games.R
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.model.data.Country
import ru.robilko.model.data.GameLeague
import ru.robilko.model.data.GameResults
import ru.robilko.model.data.GameScore
import ru.robilko.model.data.GameTeamInfo
import java.util.Date

private const val NOT_STARTED_STATUS = "Not Started"
private const val EMPTY_VALUE = "-"
private const val PRIMARY_SCORE_CELL = 0.40f
private const val SECONDARY_SCORE_CELL = 0.30f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsDialog(
    gameResults: GameResults,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AppCard(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(R.string.details_dialog_title),
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.bounceClick { onDismiss() }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.country_dialog_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(0.30f),
                        color = BasketSnapTheme.colors.secondaryText
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.70f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCard(
                            shape = CircleShape,
                            elevation = 20.dp,
                            contentPadding = PaddingValues(),
                            border = BorderStroke(1.dp, Color.Black),
                        ) {
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(gameResults.country.flagUrl)
                                    .decoderFactory(SvgDecoder.Factory())
                                    .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                                    .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        AppText(
                            text = gameResults.country.name,
                            color = BasketSnapTheme.colors.primaryText
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.league_dialog_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(0.30f),
                        color = BasketSnapTheme.colors.secondaryText
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.70f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCard(
                            shape = CircleShape,
                            elevation = 20.dp,
                            contentPadding = PaddingValues(),
                            backgroundColor = Color.White,
                            border = BorderStroke(1.dp, Color.Black),
                        ) {
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(gameResults.league.logoUrl)
                                    .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                                    .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentScale = ContentScale.Inside,
                                contentDescription = null,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        AppText(
                            text = gameResults.league.name,
                            color = BasketSnapTheme.colors.primaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.heightIn(4.dp))

                TitleItem(
                    titleResId = R.string.season_dialog_title,
                    value = gameResults.league.season
                )
                TitleItem(
                    titleResId = R.string.date_dialog_title,
                    value = gameResults.date?.toStringDate(HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN)
                        .orEmpty()
                )
                TitleItem(titleResId = R.string.venue_dialog_title, value = gameResults.venue)
                gameResults.timer?.let {
                    TitleItem(
                        titleResId = R.string.game_timer_title,
                        value = it
                    )
                }
                TitleItem(titleResId = R.string.game_status_title, value = gameResults.statusLong)

                if (gameResults.statusLong != NOT_STARTED_STATUS) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AppText(
                        text = stringResource(R.string.score_dialog_title),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    TeamsScoreHeader(
                        homeTeamName = gameResults.homeTeam.name,
                        awayTeamName = gameResults.awayTeam.name,
                        homeTeamUrl = gameResults.homeTeam.logoUrl,
                        awayTeamUrl = gameResults.awayTeam.logoUrl
                    )

                    ScoreRowItem(
                        titleResId = R.string.quarter1_dialog_title,
                        homeValue = gameResults.homeScore.quarter1,
                        awayValue = gameResults.awayScore.quarter1
                    )
                    ScoreRowItem(
                        titleResId = R.string.quarter2_dialog_title,
                        homeValue = gameResults.homeScore.quarter2,
                        awayValue = gameResults.awayScore.quarter2
                    )
                    ScoreRowItem(
                        titleResId = R.string.quarter3_dialog_title,
                        homeValue = gameResults.homeScore.quarter3,
                        awayValue = gameResults.awayScore.quarter3
                    )
                    ScoreRowItem(
                        titleResId = R.string.quarter4_dialog_title,
                        homeValue = gameResults.homeScore.quarter4,
                        awayValue = gameResults.awayScore.quarter4
                    )
                    ScoreRowItem(
                        titleResId = R.string.overtime_dialog_title,
                        homeValue = gameResults.homeScore.overTime,
                        awayValue = gameResults.awayScore.overTime
                    )
                    ScoreRowItem(
                        titleResId = R.string.total_dialog_title,
                        homeValue = gameResults.homeScore.total,
                        awayValue = gameResults.awayScore.total,
                        isTotalScore = true
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleItem(@StringRes titleResId: Int, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppText(
            text = stringResource(id = titleResId),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.weight(0.30f),
            color = BasketSnapTheme.colors.secondaryText
        )
        AppText(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.weight(0.70f),
        )
    }
}

@Composable
private fun TeamsScoreHeader(
    homeTeamName: String,
    awayTeamName: String,
    homeTeamUrl: String,
    awayTeamUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(PRIMARY_SCORE_CELL))
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colorScheme.secondary
        )
        TeamItem(name = homeTeamName, logoUrl = homeTeamUrl)
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = MaterialTheme.colorScheme.secondary
        )
        TeamItem(name = awayTeamName, logoUrl = awayTeamUrl)
    }
}

@Composable
private fun RowScope.TeamItem(
    name: String,
    logoUrl: String
) {
    Column(
        modifier = Modifier
            .weight(SECONDARY_SCORE_CELL)
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = name,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(logoUrl)
                .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentScale = ContentScale.Inside,
            contentDescription = null,
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
private fun ScoreRowItem(
    @StringRes titleResId: Int,
    homeValue: Int?,
    awayValue: Int?,
    isTotalScore: Boolean = false
) {
    Column {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.secondary,
            thickness = if (isTotalScore) 2.dp else DividerDefaults.Thickness
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = stringResource(id = titleResId),
                fontWeight = FontWeight.Bold,
                fontSize = if (isTotalScore) 18.sp else 15.sp,
                textAlign = if (isTotalScore) TextAlign.End else null,
                color = if (isTotalScore) BasketSnapTheme.colors.primaryText
                else BasketSnapTheme.colors.secondaryText,
                modifier = Modifier
                    .weight(PRIMARY_SCORE_CELL)
                    .padding(end = 8.dp)
            )
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.secondary
            )
            AppText(
                text = homeValue?.toString() ?: EMPTY_VALUE,
                fontWeight = if (isTotalScore) FontWeight.Bold else null,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = BasketSnapTheme.colors.primaryText,
                modifier = Modifier.weight(SECONDARY_SCORE_CELL)
            )
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.secondary
            )
            AppText(
                text = awayValue?.toString() ?: EMPTY_VALUE,
                fontWeight = if (isTotalScore) FontWeight.Bold else null,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = BasketSnapTheme.colors.primaryText,
                modifier = Modifier.weight(SECONDARY_SCORE_CELL)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun GameDetailsDialogPreview() {
    BasketSnapTheme(false) {
        Box(modifier = Modifier.fillMaxSize()) {
            GameDetailsDialog(
                gameResults = GameResults(
                    id = 413875,
                    date = Date(),
                    time = "02:00",
                    timestamp = 1729648800,
                    timezone = "UTC",
                    venue = "Crypto.com Arena",
                    statusLong = "Not Started",
                    statusShort = "NS",
                    timer = null,
                    league = GameLeague(
                        id = 12,
                        name = "NBA",
                        type = "League",
                        season = "2024-2025",
                        logoUrl = "https:\\/\\/media.api-sports.io\\/basketball\\/leagues\\/12.png"
                    ),
                    isPlayingNow = true,
                    country = Country(
                        id = 5,
                        name = "USA",
                        code = "US",
                        flagUrl = "https:\\/\\/media.api-sports.io\\/flags\\/us.svg"
                    ),
                    homeTeam = GameTeamInfo(
                        id = 145,
                        name = "Los Angeles Lakers",
                        logoUrl = "https:\\/\\/media.api-sports.io\\/basketball\\/teams\\/145.png"
                    ),
                    awayTeam = GameTeamInfo(
                        id = 149,
                        name = "Minnesota Timberwolves",
                        logoUrl = "https:\\/\\/media.api-sports.io\\/basketball\\/teams\\/149.png"
                    ),
                    homeScore = GameScore(
                        12,
                        13,
                        null,
                        null,
                        null,
                        25
                    ),
                    awayScore = GameScore(
                        10,
                        10,
                        null,
                        null,
                        null,
                        20
                    ),
                ),
                onDismiss = {}
            )
        }
    }
}