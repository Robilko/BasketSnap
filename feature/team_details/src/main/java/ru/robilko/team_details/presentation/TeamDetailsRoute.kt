package ru.robilko.team_details.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import ru.robilko.base_games.presentation.GameDetailsDialog
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.base_games.presentation.GamesList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppSelectableOutlinedTextField
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.ShimmerCard
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.model.data.Country
import ru.robilko.model.data.GameResults
import ru.robilko.model.data.GamesInfo
import ru.robilko.model.data.LeagueShortInfo
import ru.robilko.model.data.Points
import ru.robilko.model.data.TeamStatistics
import ru.robilko.team_details.R

@Composable
internal fun TeamDetailsRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagueDetails: (leagueId: Int) -> Unit,
    onNavigateToLeagues: (countryId: Int) -> Unit,
    onNavigateToAnotherTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamDetailsViewModel = hiltViewModel<TeamDetailsViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.team_details_page_title) }
    TeamDetailsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onFlagClick = { onNavigateToLeagues(it.id) },
        onLeagueClick = { onNavigateToLeagueDetails(it.id) },
        onNavigateToAnotherTeamDetails = onNavigateToAnotherTeamDetails,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun TeamDetailsScreen(
    uiState: TeamDetailsUiState,
    onEvent: (TeamDetailsUiEvent) -> Unit,
    onFlagClick: (Country) -> Unit,
    onLeagueClick: (LeagueShortInfo) -> Unit,
    onNavigateToAnotherTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
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
                uiState.teamStatistics?.let { teamStatistics ->
                    Details(
                        isLoadingGames = uiState.isLoadingGames,
                        games = uiState.gameResults,
                        isLoadingStatistics = uiState.isLoadingStatistics,
                        showStatistics = uiState.showStatistics,
                        showDraws = uiState.showDraws,
                        teamStatistics = teamStatistics,
                        selectedSeason = uiState.selectedSeason,
                        seasons = uiState.seasons,
                        detailsDialogState = uiState.detailsDialogState,
                        isFavourite = uiState.isFavourite,
                        onSeasonClick = { onEvent(TeamDetailsUiEvent.SeasonClick(it)) },
                        onStarIconClick = { onEvent(TeamDetailsUiEvent.StarIconClick) },
                        onFlagClick = onFlagClick,
                        onLeagueClick = onLeagueClick,
                        onGameCardClick = { onEvent(TeamDetailsUiEvent.GameCardClick(it)) },
                        onAnotherTeamClick = onNavigateToAnotherTeamDetails,
                        onDetailsDialogDismiss = { onEvent(TeamDetailsUiEvent.DetailsDialogDismiss) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Details(
    isLoadingGames: Boolean,
    games: PersistentList<GameResults>,
    isLoadingStatistics: Boolean,
    showStatistics: Boolean,
    showDraws: Boolean,
    teamStatistics: TeamStatistics,
    selectedSeason: Selectable?,
    seasons: PersistentList<Selectable>,
    detailsDialogState: GameDetailsDialogState,
    isFavourite: Boolean,
    onSeasonClick: (Selectable) -> Unit,
    onStarIconClick: () -> Unit,
    onFlagClick: (Country) -> Unit,
    onLeagueClick: (LeagueShortInfo) -> Unit,
    onGameCardClick: (GameResults) -> Unit,
    onAnotherTeamClick: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    onDetailsDialogDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GeneralTeamInfoBlock(
            teamName = teamStatistics.name,
            teamLogoUrl = teamStatistics.logoUrl,
            countryName = teamStatistics.country.name,
            countryFlagUrl = teamStatistics.country.flagUrl,
            leagueName = teamStatistics.league.name,
            leagueLogoUrl = teamStatistics.league.logoUrl,
            isFavourite = isFavourite,
            onStarIconClick = onStarIconClick,
            onFlagClick = { onFlagClick(teamStatistics.country) },
            onLeagueClick = { onLeagueClick(teamStatistics.league) }
        )

        val pagerState = rememberPagerState { TeamDetailsTabs.entries.size }
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxSize()) {
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
                TeamDetailsTabs.entries.forEachIndexed { index, tabItem ->
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

            AppSelectableOutlinedTextField(
                title = stringResource(R.string.seasons_selectable_title),
                selected = selectedSeason,
                choices = seasons,
                onSelectionChange = onSeasonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalPager(state = pagerState, userScrollEnabled = true) { pageIndex ->
                when (pageIndex) {
                    TeamDetailsTabs.GAMES.ordinal -> {
                        GamesTab(
                            isLoading = isLoadingGames,
                            games = games,
                            detailsDialogState = detailsDialogState,
                            onClick = onGameCardClick,
                            onTeamClick = { teamId, leagueId, season ->
                                if (teamId != teamStatistics.id) {
                                    onAnotherTeamClick(teamId, leagueId, season)
                                }
                            },
                            onDetailsDialogDismiss = onDetailsDialogDismiss
                        )
                    }

                    TeamDetailsTabs.STATISTICS.ordinal -> {
                        StatisticsTab(
                            isLoadingStatistics = isLoadingStatistics,
                            showStatistics = showStatistics,
                            showDraws = showDraws,
                            teamStatistics = teamStatistics
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GamesTab(
    isLoading: Boolean,
    games: PersistentList<GameResults>,
    detailsDialogState: GameDetailsDialogState,
    onClick: (GameResults) -> Unit,
    onTeamClick: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    onDetailsDialogDismiss: () -> Unit
) {
    detailsDialogState.let {
        if (it is GameDetailsDialogState.ShowData) {
            GameDetailsDialog(
                gameResults = it.gameResults,
                onDismiss = onDetailsDialogDismiss
            )
        }
    }

    GamesList(
        isLoading = isLoading,
        games = games,
        onClick = onClick,
        onTeamClick = onTeamClick,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun StatisticsTab(
    isLoadingStatistics: Boolean,
    showStatistics: Boolean,
    showDraws: Boolean,
    teamStatistics: TeamStatistics
) {
    if (isLoadingStatistics || showStatistics) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GamesStatisticsSchedule(
                isLoading = isLoadingStatistics,
                games = teamStatistics.games,
                showDraws = showDraws
            )

            PointsStatisticsSchedule(
                isLoading = isLoadingStatistics,
                points = teamStatistics.points
            )
        }
    } else NoPlayedGamesInfo()
}

@Composable
private fun GeneralTeamInfoBlock(
    teamName: String,
    teamLogoUrl: String,
    countryName: String,
    countryFlagUrl: String,
    leagueName: String,
    leagueLogoUrl: String,
    isFavourite: Boolean,
    onStarIconClick: () -> Unit,
    onFlagClick: () -> Unit,
    onLeagueClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.TopCenter),
            shadowElevation = 20.dp,
            color = MaterialTheme.colorScheme.primary,
            content = {}
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            AppText(
                text = teamName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppCard(
                        shape = CircleShape,
                        elevation = 20.dp,
                        contentPadding = PaddingValues(),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier.bounceClick { onFlagClick() }
                    ) {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(countryFlagUrl)
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
                        text = countryName,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }

                Box(modifier = Modifier.height(130.dp)) {
                    AppCard(
                        shape = CircleShape,
                        elevation = 20.dp,
                        contentPadding = PaddingValues(12.dp),
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.TopCenter),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(teamLogoUrl)
                                .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                                .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentScale = ContentScale.Fit,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    AppCard(
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp),
                        contentPadding = PaddingValues(),
                        onClick = { onStarIconClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            tint = if (isFavourite) BasketSnapTheme.colors.favouriteIcon
                            else BasketSnapTheme.colors.secondaryText,
                            contentDescription = null
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                                .data(leagueLogoUrl)
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
                        text = leagueName,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GamesStatisticsSchedule(isLoading: Boolean, games: GamesInfo, showDraws: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("GamesStatisticsSchedule")
    ) {
        ScheduleHeader(title = stringResource(R.string.games_played_title))
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.home_cell_title),
            secondCellText = games.played.home.toString()
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.away_cell_title),
            secondCellText = games.played.away.toString()
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.all_cell_title),
            secondCellText = games.played.all.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = stringResource(R.string.wins_title))
        ScheduleSubHeaders(thirdCellText = stringResource(R.string.percentage_cell_title))
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.home_cell_title),
            secondCellText = games.wins.home.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.wins.home.percentage
            )
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.away_cell_title),
            secondCellText = games.wins.away.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.wins.away.percentage
            )
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.all_cell_title),
            secondCellText = games.wins.all.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.wins.all.percentage
            )
        )

        if (isLoading || showDraws) {
            Spacer(modifier = Modifier.height(16.dp))

            ScheduleHeader(title = stringResource(R.string.draws_title))
            ScheduleSubHeaders(thirdCellText = stringResource(id = R.string.percentage_cell_title))
            ScheduleRow(
                isLoading = isLoading,
                firstCellText = stringResource(R.string.home_cell_title),
                secondCellText = games.draws.home.total.toString(),
                thirdCellText = stringResource(
                    R.string.value_with_percent_char,
                    games.draws.home.percentage
                )
            )
            ScheduleRow(
                isLoading = isLoading,
                firstCellText = stringResource(R.string.away_cell_title),
                secondCellText = games.draws.away.total.toString(),
                thirdCellText = stringResource(
                    R.string.value_with_percent_char,
                    games.draws.away.percentage
                )
            )
            ScheduleRow(
                isLoading = isLoading,
                firstCellText = stringResource(R.string.all_cell_title),
                secondCellText = games.draws.all.total.toString(),
                thirdCellText = stringResource(
                    R.string.value_with_percent_char,
                    games.draws.all.percentage
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = stringResource(R.string.loses_header_title))
        ScheduleSubHeaders(thirdCellText = stringResource(id = R.string.percentage_cell_title))
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.home_cell_title),
            secondCellText = games.loses.home.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.loses.home.percentage
            )
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.away_cell_title),
            secondCellText = games.loses.away.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.loses.away.percentage
            )
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(R.string.all_cell_title),
            secondCellText = games.loses.all.total.toString(),
            thirdCellText = stringResource(
                R.string.value_with_percent_char,
                games.loses.all.percentage
            )
        )
    }
}

@Composable
private fun PointsStatisticsSchedule(isLoading: Boolean, points: Points) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScheduleHeader(title = stringResource(R.string.points_for_header_title))
        ScheduleSubHeaders(thirdCellText = stringResource(R.string.average_cell_title))
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.home_cell_title),
            secondCellText = points.forTeam.total.home.toString(),
            thirdCellText = points.forTeam.average.home
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.away_cell_title),
            secondCellText = points.forTeam.total.away.toString(),
            thirdCellText = points.forTeam.average.away
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.all_cell_title),
            secondCellText = points.forTeam.total.all.toString(),
            thirdCellText = points.forTeam.average.all
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = stringResource(R.string.points_against_header_title))
        ScheduleSubHeaders(thirdCellText = stringResource(id = R.string.average_cell_title))
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.home_cell_title),
            secondCellText = points.againstTeam.total.home.toString(),
            thirdCellText = points.againstTeam.average.home
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.away_cell_title),
            secondCellText = points.againstTeam.total.away.toString(),
            thirdCellText = points.againstTeam.average.away
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = stringResource(id = R.string.all_cell_title),
            secondCellText = points.againstTeam.total.all.toString(),
            thirdCellText = points.againstTeam.average.all
        )
    }
}

@Composable
private fun ScheduleHeader(title: String) {
    Card(
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        AppText(
            text = title,
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ScheduleSubHeaders(thirdCellText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScheduleCell(
            text = "",
            modifier = Modifier.fillMaxWidth(0.25f),
            containerColor = Color.Gray
        )
        ScheduleCell(
            text = stringResource(R.string.total_cell_title),
            modifier = Modifier.weight(1f),
            fontStyle = FontStyle.Italic,
            containerColor = Color.Gray
        )
        ScheduleCell(
            text = thirdCellText,
            modifier = Modifier.weight(1f),
            fontStyle = FontStyle.Italic,
            containerColor = Color.Gray
        )
    }
}


@Composable
private fun ScheduleRow(
    isLoading: Boolean,
    firstCellText: String,
    secondCellText: String,
    thirdCellText: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            ShimmerCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(21.dp),
                shape = RoundedCornerShape(20)
            )
        } else {
            ScheduleCell(
                text = firstCellText,
                modifier = Modifier.fillMaxWidth(0.25f),
                fontStyle = FontStyle.Italic,
                containerColor = Color.Gray
            )
            ScheduleCell(
                text = secondCellText,
                modifier = Modifier.weight(1f),
            )
            thirdCellText?.let {
                ScheduleCell(text = it, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ScheduleCell(
    text: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight? = null,
    contentPaddings: PaddingValues = PaddingValues(),
    containerColor: Color = Color.LightGray
) {
    Card(
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        AppText(
            text = text,
            fontSize = 14.sp,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            modifier = Modifier
                .padding(contentPaddings)
                .align(Alignment.CenterHorizontally),
            color = Color.Black
        )
    }
}

@Composable
private fun NoPlayedGamesInfo() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_games),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(BasketSnapTheme.colors.primaryText)
            )
            AppText(text = stringResource(R.string.the_team_has_not_played_this_season_yet))
        }
    }
}