package ru.robilko.league_details.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.toPersistentList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.league_details.R
import ru.robilko.model.data.League
import ru.robilko.model.data.Season
import ru.robilko.core_ui.R as R_core_ui

@Composable
internal fun LeagueDetailsRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeams: (leagueId: Int, season: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeagueDetailsViewModel = hiltViewModel<LeagueDetailsViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.league_details_page_title) }
    LeagueDetailsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToTeams = onNavigateToTeams,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LeagueDetailsScreen(
    uiState: LeagueDetailsUiState,
    onEvent: (LeagueDetailsUiEvent) -> Unit,
    onNavigateToTeams: (leagueId: Int, season: String) -> Unit,
    modifier: Modifier = Modifier
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

            DataState.Success -> uiState.league?.let {
                Details(
                    league = it,
                    isFavourite = uiState.isFavourite,
                    onSeasonClick = { season -> onEvent(LeagueDetailsUiEvent.SeasonClick(season)) },
                    onStarIconClick = { onEvent(LeagueDetailsUiEvent.StarIconClick) }
                )

                uiState.selectedSeason?.let { season ->
                    SeasonDialog(
                        onDismissRequest = { onEvent(LeagueDetailsUiEvent.DismissSeasonDialog) },
                        onTeamsClick = {
                            onEvent(LeagueDetailsUiEvent.DismissSeasonDialog)
                            onNavigateToTeams(it.id, season)
                        },
                        onGamesClick = {
                            onEvent(LeagueDetailsUiEvent.DismissSeasonDialog)
                            //todo add games navigation
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Details(
    league: League,
    isFavourite: Boolean,
    onSeasonClick: (Season) -> Unit,
    onStarIconClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("LeagueDetails"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(league.logoUrl)
                        .error(R_core_ui.drawable.ic_no_image_placeholder)
                        .placeholder(R_core_ui.drawable.ic_image_loader)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .size(100.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    InfoTextBlock(titleResId = R.string.league_name, value = league.name)
                    InfoTextBlock(titleResId = R.string.league_type, value = league.type)
                    InfoTextBlock(
                        titleResId = R.string.league_country,
                        value = league.country.name
                    )
                }

                IconButton(onClick = { onStarIconClick() }) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        tint = if (isFavourite) BasketSnapTheme.colors.favouriteIcon
                        else BasketSnapTheme.colors.secondaryText,
                        contentDescription = null
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 38.dp))
        }
        item {
            AppText(
                text = stringResource(id = R.string.seasons_title),
                color = BasketSnapTheme.colors.secondaryText,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        items(league.seasons.toPersistentList(), key = { it.season }) { season ->
            SeasonItem(
                season = season,
                onClick = { onSeasonClick(season) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InfoTextBlock(@StringRes titleResId: Int, value: String) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppText(
            text = stringResource(id = titleResId),
            color = BasketSnapTheme.colors.secondaryText,
            fontStyle = FontStyle.Italic
        )
        AppText(text = value)
    }
}

@Composable
private fun SeasonItem(season: Season, onClick: () -> Unit) {
    AppCard(
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier
            .bounceClick { onClick() }
            .testTag("SeasonItemCard")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = season.season,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonDialog(
    onDismissRequest: () -> Unit,
    onTeamsClick: () -> Unit,
    onGamesClick: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        AppCard(modifier = Modifier
            .padding(48.dp)
            .testTag("SeasonDialog")) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppText(
                    text = stringResource(id = R.string.teams_dialog_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick { onTeamsClick() }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                AppText(
                    text = stringResource(id = R.string.games_dialog_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
//                        .bounceClick { onGamesClick() } //todo add games
                    color = Color.LightGray,
                )
            }
        }
    }
}