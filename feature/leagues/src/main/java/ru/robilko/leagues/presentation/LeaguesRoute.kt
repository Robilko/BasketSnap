package ru.robilko.leagues.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.leagues.R
import ru.robilko.model.data.League
import ru.robilko.model.data.Season

@Composable
internal fun LeaguesRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaguesViewModel = hiltViewModel<LeaguesViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.leagues_page_title) }
    LeaguesScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LeaguesScreen(
    uiState: LeaguesUiState,
    onEvent: (LeaguesUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (uiState.dataState) {
            LeaguesDataState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            is LeaguesDataState.Error -> {
                ErrorScreen(
                    text = uiState.dataState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetryButtonClick = uiState.dataState.onRetryAction
                )
            }

            LeaguesDataState.Leagues -> {
                LeaguesList(
                    leagues = uiState.leagues,
                    onClick = { onEvent(LeaguesUiEvent.LeagueCardClick(it)) },
                    onStarIconClick = { onEvent(LeaguesUiEvent.StarIconClick(it))}
                )
                uiState.selectedLeague?.let { league ->
                    LeagueDetailsDialog(
                        league = league,
                        onDismiss = { onEvent(LeaguesUiEvent.DismissDetailsDialog) },
                        onClick = { onEvent(LeaguesUiEvent.SeasonClick(it, league)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaguesList(
    leagues: PersistentList<League>,
    onClick: (League) -> Unit,
    onStarIconClick: (League) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(leagues, key = { it.id }) { league ->
            AppCard(
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 16.dp),
                onClick = { onClick(league) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(league.logoUrl)
                            .error(R.drawable.ic_league_placeholder)
                            .placeholder(R.drawable.ic_league_placeholder)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(64.dp)
                    )
                    Text(text = league.name, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onStarIconClick(league) }) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            tint = BasketSnapTheme.colors.secondaryText,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeagueDetailsDialog(league: League, onDismiss: () -> Unit, onClick: (Season) -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        AppCard(contentPadding = PaddingValues(horizontal = 16.dp)) {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AppText(
                        text = stringResource(id = R.string.league_details_title),
                        color = BasketSnapTheme.colors.secondaryText,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 38.dp)
                        .padding(bottom = 8.dp)
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(league.logoUrl)
                            .error(R.drawable.ic_league_placeholder)
                            .placeholder(R.drawable.ic_league_placeholder)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(64.dp)
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
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 38.dp))
                Seasons(seasons = league.seasons.toPersistentList(), onClick = onClick)
            }
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
private fun Seasons(seasons: PersistentList<Season>, onClick: (Season) -> Unit) {
    Column {
        AppText(
            text = stringResource(id = R.string.seasons_title),
            color = BasketSnapTheme.colors.secondaryText,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            seasons.forEach { season ->
                AppCard(contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(season) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = season.season,
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}