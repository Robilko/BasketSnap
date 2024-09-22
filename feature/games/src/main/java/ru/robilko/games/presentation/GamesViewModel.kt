package ru.robilko.games.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.SERVER_DATE_PATTERN
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base.util.toStringDate
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
import java.util.Date
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
        getGamesResults(season = initialSeason)
        getSeasons()
    }

    override fun onEvent(event: GamesUiEvent) {
        when (event) {
            is GamesUiEvent.SeasonClick -> makeActionOnSeasonClick(event.season)
            is GamesUiEvent.GameCardClick -> showDetailsDialog(event.gameResults)
            GamesUiEvent.DetailsDialogDismiss -> closeDetailsDialog()
        }
    }

    private fun getGamesResults(season: String, date: String? = null, isFetching: Boolean = false) {
        viewModelScope.launch {
            if (!isFetching) _uiState.update { it.copy(dataState = DataState.Loading) }
            getGamesResultsUseCase(
                leagueId = leagueId,
                season = season,
                date = date
            ).apply {
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            gameResults = getFinalGamesResults(
                                newResults = response.data.sortedBy { game -> game.date },
                                isFetching = isFetching
                            )
                        )
                    }
                    checkNeedToFetchGameResults()
                }
                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = {
                                    getGamesResults(
                                        season = season,
                                        isFetching = isFetching
                                    )
                                }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getFinalGamesResults(
        newResults: List<GameResults>,
        isFetching: Boolean
    ): PersistentList<GameResults> {
        if (!isFetching) return newResults.toPersistentList()

        val currentList = _uiState.value.gameResults.toMutableList()
        newResults.forEach { game ->
            val index = currentList.indexOfFirst { game.id == it.id }
            if (index != -1) currentList[index] = game
        }

        return currentList.toPersistentList()
    }

    private fun getSeasons() {
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
                                onRetryAction = ::getSeasons
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

    private fun checkNeedToFetchGameResults() {
        viewModelScope.launch {
            val hasGamesToCheck = _uiState.value.gameResults.any { it.isPlayingNow }

            if (hasGamesToCheck) {
                delay(GAMES_FETCH_TIMEOUT)
                getGamesResults(
                    season = _uiState.value.selectedSeason?.value ?: return@launch,
                    date = Date().toStringDate(SERVER_DATE_PATTERN),
                    isFetching = true
                )
            } else return@launch
        }
    }

    private fun makeActionOnSeasonClick(season: Selectable) {
        _uiState.update { it.copy(selectedSeason = season) }
        getGamesResults(season = season.value)
    }

    private fun showDetailsDialog(gameResults: GameResults) {
        _uiState.update {
            it.copy(detailsDialogState = GameDetailsDialogState.ShowData(gameResults))
        }
    }

    private fun closeDetailsDialog() {
        _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.None) }
    }

    private companion object {
        const val GAMES_FETCH_TIMEOUT = 60_000L
    }
}