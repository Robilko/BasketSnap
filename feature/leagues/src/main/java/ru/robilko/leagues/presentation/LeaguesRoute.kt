package ru.robilko.leagues.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.leagues.R
import ru.robilko.model.data.League
import ru.robilko.core_ui.R as R_core_ui

@Composable
internal fun LeaguesRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaguesViewModel = hiltViewModel<LeaguesViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.leagues_page_title) }
    LeaguesScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToLeagueDetails = onNavigateToLeagueDetails,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LeaguesScreen(
    uiState: LeaguesUiState,
    onEvent: (LeaguesUiEvent) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit,
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

            DataState.Success -> {
                LeaguesList(
                    favouriteLeaguesIds = uiState.favouriteLeaguesIds,
                    leagues = uiState.leagues,
                    onClick = onNavigateToLeagueDetails,
                    onStarIconClick = { league, isFavourite ->
                        onEvent(LeaguesUiEvent.StarIconClick(league, isFavourite))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LeaguesList(
    favouriteLeaguesIds: PersistentList<Int>,
    leagues: PersistentList<League>,
    onClick: (League) -> Unit,
    onStarIconClick: (league: League, isFavourite: Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(leagues, key = { it.id }) { league ->
            val isFavourite = favouriteLeaguesIds.any { it == league.id }
            AppCard(
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 16.dp),
                onClick = { onClick(league) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            .size(64.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppText(text = league.name)
                        HorizontalDivider(Modifier.padding(vertical = 4.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            AppText(
                                text = stringResource(id = R.string.country_title),
                                color = BasketSnapTheme.colors.secondaryText,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                            AppText(text = league.country.name, fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = { onStarIconClick(league, isFavourite) }) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            tint = if (isFavourite) BasketSnapTheme.colors.favouriteIcon
                            else BasketSnapTheme.colors.secondaryText,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}