package ru.robilko.team_details.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
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
    teamStatistics: TeamStatistics,
    selectedSeason: String,
    seasons: List<String>,
    isFavourite: Boolean,
    onSeasonClick: (String) -> Unit,
    onStarIconClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GeneralTeamInfoBlock(
            teamName = teamStatistics.name,
            teamLogoUrl = teamStatistics.logoUrl,
            countryFlagUrl = teamStatistics.country.flagUrl,
            leagueLogoUrl = teamStatistics.league.logoUrl,
            isFavourite = isFavourite,
            onStarIconClick = onStarIconClick
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
                    border = BorderStroke(1.dp, BasketSnapTheme.colors.primaryText),
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
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.TopCenter),
                        border = BorderStroke(2.dp, BasketSnapTheme.colors.primaryText)
                    ) {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(teamLogoUrl)
                                .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                                .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                                .build(),
                            contentScale = ContentScale.Inside,
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
                    border = BorderStroke(1.dp, BasketSnapTheme.colors.primaryText),
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