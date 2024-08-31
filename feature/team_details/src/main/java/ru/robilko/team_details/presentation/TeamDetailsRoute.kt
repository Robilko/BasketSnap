package ru.robilko.team_details.presentation

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.ShimmerCard
import ru.robilko.model.data.GamesInfo
import ru.robilko.model.data.Points
import ru.robilko.model.data.TeamStatistics
import ru.robilko.team_details.R

@Composable
internal fun TeamDetailsRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamDetailsViewModel = hiltViewModel<TeamDetailsViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.team_details_page_title) }
    TeamDetailsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun TeamDetailsScreen(
    uiState: TeamDetailsUiState,
    onEvent: (TeamDetailsUiEvent) -> Unit,
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
                uiState.teamStatistics?.let {
                    Details(
                        isLoadingStatistics = uiState.isLoadingStatistics,
                        teamStatistics = it,
                        selectedSeason = uiState.selectedSeason.orEmpty(),
                        seasons = uiState.seasons,
                        isFavourite = uiState.isFavourite,
                        onSeasonClick = { onEvent(TeamDetailsUiEvent.SeasonClick(it)) },
                        onStarIconClick = { onEvent(TeamDetailsUiEvent.StarIconClick) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Details(
    isLoadingStatistics: Boolean,
    teamStatistics: TeamStatistics,
    selectedSeason: String,
    seasons: List<String>,
    isFavourite: Boolean,
    onSeasonClick: (String) -> Unit,
    onStarIconClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GeneralTeamInfoBlock(
            teamName = teamStatistics.name,
            teamLogoUrl = teamStatistics.logoUrl,
            countryFlagUrl = teamStatistics.country.flagUrl,
            leagueLogoUrl = teamStatistics.league.logoUrl,
            isFavourite = isFavourite,
            onStarIconClick = onStarIconClick
        )

        GamesStatisticsSchedule(
            isLoading = isLoadingStatistics,
            games = teamStatistics.games
        )

        PointsStatisticsSchedule(
            isLoading = isLoadingStatistics,
            points = teamStatistics.points
        )
    }
}

@Composable
private fun GeneralTeamInfoBlock(
    teamName: String,
    teamLogoUrl: String,
    countryFlagUrl: String,
    leagueLogoUrl: String,
    isFavourite: Boolean,
    onStarIconClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.TopCenter),
            shadowElevation = 20.dp,
            color = MaterialTheme.colorScheme.primary,
            content = {}
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                AppCard(
                    shape = CircleShape,
                    elevation = 20.dp,
                    contentPadding = PaddingValues(),
                    border = BorderStroke(1.dp, Color.Black),
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(countryFlagUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                            .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
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
                            .data(leagueLogoUrl)
                            .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                            .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                            .build(),
                        contentScale = ContentScale.Inside,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GamesStatisticsSchedule(isLoading: Boolean, games: GamesInfo) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScheduleHeader(title = "Games played")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = games.played.home.toString()
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = games.played.away.toString()
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
            secondCellText = games.played.all.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = "Wins")
        ScheduleSubHeaders(firstHeaderText = "total", secondHeaderText = "percentage")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = games.wins.home.total.toString(),
            thirdCellText = games.wins.home.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = games.wins.away.total.toString(),
            thirdCellText = games.wins.away.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
            secondCellText = games.wins.all.total.toString(),
            thirdCellText = games.wins.all.percentage
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = "Draws")
        ScheduleSubHeaders(firstHeaderText = "total", secondHeaderText = "percentage")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = games.draws.home.total.toString(),
            thirdCellText = games.draws.home.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = games.draws.away.total.toString(),
            thirdCellText = games.draws.away.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
            secondCellText = games.draws.all.total.toString(),
            thirdCellText = games.draws.all.percentage
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = "Loses")
        ScheduleSubHeaders(firstHeaderText = "total", secondHeaderText = "percentage")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = games.loses.home.total.toString(),
            thirdCellText = games.loses.home.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = games.loses.away.total.toString(),
            thirdCellText = games.loses.away.percentage
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
            secondCellText = games.loses.all.total.toString(),
            thirdCellText = games.loses.all.percentage
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
        ScheduleHeader(title = "Points for")
        ScheduleSubHeaders(firstHeaderText = "total", secondHeaderText = "average")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = points.forTeam.total.home.toString(),
            thirdCellText = points.forTeam.average.home
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = points.forTeam.total.away.toString(),
            thirdCellText = points.forTeam.average.away
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
            secondCellText = points.forTeam.total.all.toString(),
            thirdCellText = points.forTeam.average.all
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScheduleHeader(title = "Points against")
        ScheduleSubHeaders(firstHeaderText = "total", secondHeaderText = "average")
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Home:",
            secondCellText = points.againstTeam.total.home.toString(),
            thirdCellText = points.againstTeam.average.home
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "Away:",
            secondCellText = points.againstTeam.total.away.toString(),
            thirdCellText = points.againstTeam.average.away
        )
        ScheduleRow(
            isLoading = isLoading,
            firstCellText = "All:",
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
                .padding(16.dp)
        )
    }
}

@Composable
private fun ScheduleSubHeaders(
    firstHeaderText: String,
    secondHeaderText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.fillMaxWidth(0.25f))
        ScheduleCell(
            text = firstHeaderText,
            modifier = Modifier.weight(1f),
            fontStyle = FontStyle.Italic
        )
        ScheduleCell(
            text = secondHeaderText,
            modifier = Modifier.weight(1f),
            fontStyle = FontStyle.Italic
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
                    .height(42.dp),
                shape = RoundedCornerShape(20)
            )
        } else {
            ScheduleCell(
                text = firstCellText,
                modifier = Modifier.fillMaxWidth(0.25f),
                fontStyle = FontStyle.Italic
            )
            ScheduleCell(text = secondCellText, modifier = Modifier.weight(1f))
            thirdCellText?.let { ScheduleCell(text = it, modifier = Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun ScheduleCell(
    text: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle = FontStyle.Normal
) {
    Card(
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        modifier = modifier
    ) {
        AppText(
            text = text,
            fontSize = 14.sp,
            fontStyle = fontStyle,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Gray
        )
    }
}