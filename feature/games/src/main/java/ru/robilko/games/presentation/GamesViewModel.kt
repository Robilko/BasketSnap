package ru.robilko.games.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base_games.domain.useCases.GetGamesResultsUseCase
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.base_seasons.domain.useCases.GetSeasonsUseCase
import ru.robilko.core_ui.R
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.asSelectableData
import ru.robilko.games.navigation.LEAGUE_ID_ARG
import ru.robilko.games.navigation.SEASON_ARG
import ru.robilko.model.data.GameResults
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getGamesResultsUseCase: GetGamesResultsUseCase,
    private val getSeasonsUseCase: GetSeasonsUseCase,
) : BaseAppViewModel<GamesUiState, GamesUiEvent>() {
    private val leagueId = checkNotNull<Int>(savedStateHandle[LEAGUE_ID_ARG])
    private val initialSeason = checkNotNull<String>(savedStateHandle[SEASON_ARG])

    private val _uiState: MutableStateFlow<GamesUiState> = MutableStateFlow(GamesUiState())
    override val uiState: StateFlow<GamesUiState> = _uiState

    init {
        getGamesResults(initialSeason)
        getLeagueSeasons()
    }

    override fun onEvent(event: GamesUiEvent) {
        when (event) {
            is GamesUiEvent.SeasonClick -> makeActionOnSeasonClick(event.season)
            is GamesUiEvent.GameCardClick -> showDetailsDialog(event.gameResults)
            GamesUiEvent.DetailsDialogDismiss -> closeDetailsDialog()
        }
    }

    private fun getGamesResults(season: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(dataState = DataState.Loading) }
            getGamesResultsUseCase(
                leagueId = leagueId,
                season = season
            ).apply {
                onSuccess { response ->
                    val gameResults =
                        response.data.sortedBy { it.date }.toPersistentList()
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            gameResults = gameResults
                        )
                    }
                }
                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = { getGamesResults(season) }
                            )
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
                            ),
                            showSeasons = false
                        )
                    }
                }
                onSuccess { response ->
                    val selectedSeason = initialSeason
                    val seasonChoices =
                        response.data.map { season -> season.season.asSelectableData() }
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            showSeasons = true,
                            seasons = seasonChoices.toPersistentList(),
                            selectedSeason = selectedSeason.asSelectableData()
                        )
                    }
                }
            }
        }
    }

    private fun makeActionOnSeasonClick(season: Selectable) {
        _uiState.update { it.copy(selectedSeason = season) }
        getGamesResults(season.value)
    }

    private fun showDetailsDialog(gameResults: GameResults) {
        _uiState.update {
            it.copy(detailsDialogState = GameDetailsDialogState.ShowData(gameResults))
        }
    }

    private fun closeDetailsDialog() {
        _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.None) }
    }
}