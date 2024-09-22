package ru.robilko.base_games.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GameDetailsDialog(
    gameResults: GameResults,
    onCountryClick: () -> Unit,
    onLeagueClick: () -> Unit,
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
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.bounceClick { onDismiss() }
                    )
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCard(
                            shape = CircleShape,
                            elevation = 20.dp,
                            contentPadding = PaddingValues(),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.bounceClick { onCountryClick() }
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
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        AppText(
                            text = gameResults.country.name,
                            color = BasketSnapTheme.colors.primaryText
                        )
                    }


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCard(
                            shape = CircleShape,
                            elevation = 20.dp,
                            contentPadding = PaddingValues(),
                            backgroundColor = Color.White,
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier.bounceClick { onLeagueClick() }
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
                                modifier = Modifier.size(40.dp)
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
            }
        }
    }
}

@Composable
private fun TitleItem(@StringRes titleResId: Int, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppText(
            text = stringResource(id = titleResId),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = BasketSnapTheme.colors.secondaryText
        )
        AppText(
            text = value,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
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
                onCountryClick = {},
                onLeagueClick = {},
                onDismiss = {}
            )
        }
    }
}