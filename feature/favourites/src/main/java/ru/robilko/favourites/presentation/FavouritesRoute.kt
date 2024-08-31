package ru.robilko.favourites.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import kotlinx.coroutines.launch
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.favourites.R
import ru.robilko.model.data.League
import ru.robilko.model.data.TeamInfo
import ru.robilko.core_ui.R as R_core_ui

@Composable
internal fun FavouritesRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit,
    onNavigateToTeamDetails: (TeamInfo) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel = hiltViewModel<FavouritesViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.favourites_title) }
    FavouritesScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToLeagueDetails = onNavigateToLeagueDetails,
        onNavigateToTeamDetails = onNavigateToTeamDetails,
        modifier = modifier
    )
}

@Composable
private fun FavouritesScreen(
    uiState: FavouritesUiState,
    onEvent: (FavouritesUiEvent) -> Unit,
    onNavigateToLeagueDetails: (League) -> Unit,
    onNavigateToTeamDetails: (TeamInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { FavouritesTabs.entries.size }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = BasketSnapTheme.colors.primaryText
                )
            }
        ) {
            FavouritesTabs.entries.forEachIndexed { index, tabItem ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    content = {
                        AppText(
                            text = stringResource(id = tabItem.titleResId),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                )
            }
        }

        HorizontalPager(state = pagerState, userScrollEnabled = true) { pageIndex ->
            when (pageIndex) {
                FavouritesTabs.LEAGUES.ordinal -> {
                    LeaguesPage(
                        isLoading = uiState.isLeaguesLoading,
                        data = uiState.leagues,
                        onClick = onNavigateToLeagueDetails,
                        onDeleteIconClick = { onEvent(FavouritesUiEvent.DeleteLeagueIconClick(it)) }
                    )
                }

                FavouritesTabs.TEAMS.ordinal -> {
                    TeamsPage(
                        isLoading = uiState.isTeamsLoading,
                        data = uiState.teams,
                        onClick = onNavigateToTeamDetails,
                        onDeleteIconClick = { onEvent(FavouritesUiEvent.DeleteTeamIconClick(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaguesPage(
    isLoading: Boolean,
    data: PersistentList<League>,
    onClick: (League) -> Unit,
    onDeleteIconClick: (League) -> Unit
) {
    when {
        isLoading -> LoadingIndicator()
        data.isEmpty() -> EmptyList(textResId = R.string.empty_favourite_leagues)
        else -> LeaguesList(data = data, onClick = onClick, onDeleteIconClick = onDeleteIconClick)
    }
}

@Composable
private fun TeamsPage(
    isLoading: Boolean,
    data: PersistentList<TeamInfo>,
    onClick: (TeamInfo) -> Unit,
    onDeleteIconClick: (TeamInfo) -> Unit
) {
    when {
        isLoading -> LoadingIndicator()
        data.isEmpty() -> EmptyList(textResId = R.string.empty_favourite_leagues)
        else -> TeamsList(data = data, onClick = onClick, onDeleteIconClick = onDeleteIconClick)
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun EmptyList(@StringRes textResId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(BasketSnapTheme.colors.primaryText)
            )
            AppText(text = stringResource(id = textResId))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LeaguesList(
    data: PersistentList<League>,
    onClick: (League) -> Unit,
    onDeleteIconClick: (League) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data, key = { it.id }) { league ->
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
                        if (league.country.name.isNotEmpty()) {
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
                    }

                    IconButton(onClick = { onDeleteIconClick(league) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = BasketSnapTheme.colors.secondaryText,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TeamsList(
    data: PersistentList<TeamInfo>,
    onClick: (TeamInfo) -> Unit,
    onDeleteIconClick: (TeamInfo) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data, key = { it.id }) { teamInfo ->
            AppCard(
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 16.dp),
                onClick = { onClick(teamInfo) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(teamInfo.logoUrl)
                            .error(R_core_ui.drawable.ic_no_image_placeholder)
                            .placeholder(R_core_ui.drawable.ic_image_loader)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(64.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppText(text = teamInfo.name)
                        HorizontalDivider(Modifier.padding(vertical = 4.dp))
                        if (teamInfo.country.name.isNotEmpty()) {
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                AppText(
                                    text = stringResource(id = R.string.country_title),
                                    color = BasketSnapTheme.colors.secondaryText,
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic
                                )
                                AppText(text = teamInfo.country.name, fontSize = 12.sp)
                            }
                        }
                    }

                    IconButton(onClick = { onDeleteIconClick(teamInfo) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = BasketSnapTheme.colors.secondaryText,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}