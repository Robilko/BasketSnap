package ru.robilko.teams.presentation

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.model.data.TeamInfo
import ru.robilko.teams.R

@Composable
internal fun TeamsRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToTeamDetails: (TeamInfo) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamsViewModel = hiltViewModel<TeamsViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.teams_page_title) }
    TeamsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToTeamDetails = onNavigateToTeamDetails,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun TeamsScreen(
    uiState: TeamsUiState,
    onEvent: (TeamsUiEvent) -> Unit,
    onNavigateToTeamDetails: (TeamInfo) -> Unit,
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
                TeamsList(
                    teams = uiState.teams,
                    favouriteTeamsIds = uiState.favouriteTeamsIds,
                    onClick = onNavigateToTeamDetails,
                    onStarIconClick = { team, isFavourite ->
                        onEvent(TeamsUiEvent.StarIconClick(team, isFavourite))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TeamsList(
    teams: PersistentList<TeamInfo>,
    favouriteTeamsIds: PersistentList<Int>,
    onClick: (TeamInfo) -> Unit,
    onStarIconClick: (team: TeamInfo, isFavourite: Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("TeamsList"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(teams, key = { it.id }) { team ->
            val isFavourite = favouriteTeamsIds.any { it == team.id }
            AppCard(
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 16.dp),
                onClick = { onClick(team) },
                modifier = Modifier.testTag("TeamCard")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(team.logoUrl)
                            .error(ru.robilko.core_ui.R.drawable.ic_no_image_placeholder)
                            .placeholder(ru.robilko.core_ui.R.drawable.ic_image_loader)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .networkCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(64.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppText(text = team.name)
                        HorizontalDivider(Modifier.padding(vertical = 4.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            AppText(
                                text = stringResource(id = R.string.country_title),
                                color = BasketSnapTheme.colors.secondaryText,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                            AppText(text = team.country.name, fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = { onStarIconClick(team, isFavourite) }) {
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