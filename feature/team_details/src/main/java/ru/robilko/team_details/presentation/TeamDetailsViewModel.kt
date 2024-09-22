package ru.robilko.team_details.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base_favourites.domain.useCases.AddTeamToFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.DeleteTeamFromFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteTeamsUseCase
import ru.robilko.base_games.domain.useCases.GetGamesResultsUseCase
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.base_seasons.domain.useCases.GetSeasonsUseCase
import ru.robilko.core_ui.R
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.asSelectableData
import ru.robilko.model.data.TeamInfo
import ru.robilko.team_details.domain.useCases.GetTeamStatisticsUseCase
import ru.robilko.team_details.navigation.LEAGUE_ID_ARG
import ru.robilko.team_details.navigation.SEASON_ARG
import ru.robilko.team_details.navigation.TEAM_ID_ARG
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val getGamesResultsUseCase: GetGamesResultsUseCase,
    private val getTeamStatisticsUseCase: GetTeamStatisticsUseCase,
    private val getFavouriteTeamsUseCase: GetFavouriteTeamsUseCase,
    private val addTeamToFavouritesUseCase: AddTeamToFavouritesUseCase,
    private val deleteTeamFromFavouritesUseCase: DeleteTeamFromFavouritesUseCase
) : BaseAppViewModel<TeamDetailsUiState, TeamDetailsUiEvent>() {
    private val teamId = checkNotNull<Int>(savedStateHandle[TEAM_ID_ARG])
    private val leagueId = checkNotNull<Int>(savedStateHandle[LEAGUE_ID_ARG])
    private var initialSeason = savedStateHandle.get<String>(SEASON_ARG)

    private val _uiState = MutableStateFlow(TeamDetailsUiState())
    override val uiState: StateFlow<TeamDetailsUiState> = _uiState

    override fun onEvent(event: TeamDetailsUiEvent) {
        when (event) {
            is TeamDetailsUiEvent.SeasonClick -> makeActionOnSeasonClick(event.season)
            TeamDetailsUiEvent.StarIconClick -> makeActionOnStarIconClick()
            TeamDetailsUiEvent.DetailsDialogDismiss ->
                _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.None) }

            is TeamDetailsUiEvent.GameCardClick ->
                _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.ShowData(event.gameResults)) }
        }
    }

    init {
        observeFavouriteTeams()
        getLeagueSeasons()
    }

    private fun observeFavouriteTeams() {
        viewModelScope.launch {
            getFavouriteTeamsUseCase().collect { response ->
                if (response is Response.Success) {
                    _uiState.update {
                        it.copy(
                            isFavourite = response.data.any { team -> team.id == teamId && team.leagueId == leagueId }
                        )
                    }
                }
            }
        }
    }

    private fun getLeagueSeasons() {
        viewModelScope.launch {
            _uiState.update { it.copy(dataState = DataState.Loading) }
            getSeasonsUseCase(leagueId = leagueId).apply {
                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = ::getLeagueSeasons
                            )
                        )
                    }
                }
                onSuccess { response ->
                    val selectedSeason = initialSeason ?: response.data.firstOrNull()?.season
                    val seasonChoices =
                        response.data.map { season -> season.season.asSelectableData() }
                    _uiState.update {
                        it.copy(
                            dataState = if (initialSeason == null) DataState.Loading else DataState.Success,
                            seasons = seasonChoices.toPersistentList(),
                            selectedSeason = selectedSeason?.asSelectableData()
                        )
                    }
                    getGameResults()
                    getTeamStatistics()
                }
            }
        }
    }

    private fun getGameResults(isFetch: Boolean = false) {
        val selectedSeason = _uiState.value.selectedSeason ?: return
        if (isFetch) _uiState.update { it.copy(isLoadingGames = true) }

        viewModelScope.launch {
            getGamesResultsUseCase(
                leagueId = leagueId,
                season = selectedSeason.value,
                teamId = teamId
            ).apply {
                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = ::getGameResults
                            )
                        )
                    }
                }
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            isLoadingGames = false,
                            gameResults = response.data.toPersistentList()
                        )
                    }
                    checkNeedToFetchGameResults()
                }
            }
        }
    }

    private fun checkNeedToFetchGameResults() {
        viewModelScope.launch {
            val hasGamesToCheck = _uiState.value.gameResults.any { it.isPlayingNow }

            if (hasGamesToCheck) {
                delay(60_000)
                getGameResults(true)
            } else return@launch
        }
    }

    private fun getTeamStatistics() {
        val selectedSeason = _uiState.value.selectedSeason ?: return
        _uiState.update { it.copy(isLoadingStatistics = true) }

        viewModelScope.launch {
            getTeamStatisticsUseCase(
                season = selectedSeason.value,
                leagueId = leagueId,
                teamId = teamId
            ).apply {
                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = ::getTeamStatistics
                            )
                        )
                    }
                }
                onSuccess { response ->
                    val hasPlayedGames = response.data?.let { it.games.played.all > 0 } ?: false

                    val showDraws = response.data?.let { it.games.draws.all.total > 0 } ?: false
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            isLoadingStatistics = false,
                            teamStatistics = response.data,
                            showStatistics = hasPlayedGames,
                            showDraws = showDraws
                        )
                    }
                }
            }
        }
    }

    private fun makeActionOnSeasonClick(season: Selectable) {
        if (initialSeason == null) initialSeason = season.value
        _uiState.update { it.copy(selectedSeason = season) }
        getGameResults()
        getTeamStatistics()
    }

    private fun makeActionOnStarIconClick() {
        viewModelScope.launch {
            if (_uiState.value.isFavourite) deleteTeamFromFavouritesUseCase(teamId, leagueId)
            else _uiState.value.teamStatistics?.let {
                val teamInfo = TeamInfo(
                    id = it.id,
                    name = it.name,
                    logoUrl = it.logoUrl,
                    country = it.country,
                    leagueId = it.league.id,
                    leagueName = it.league.name
                )
                addTeamToFavouritesUseCase(teamInfo)
            }
        }
    }
}