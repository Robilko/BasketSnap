package ru.robilko.games.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.SERVER_DATE_PATTERN
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base.util.toStringDate
import ru.robilko.core_ui.R
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.games.domain.useCases.GetGamesResultsUseCase
import ru.robilko.games.navigation.LEAGUE_ID_ARG
import ru.robilko.games.navigation.SEASON_ARG
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getGamesResultsUseCase: GetGamesResultsUseCase
) : BaseAppViewModel<GamesUiState, GamesUiEvent>() {
    private val leagueId = checkNotNull<Int>(savedStateHandle[LEAGUE_ID_ARG])
    private val season = checkNotNull<String>(savedStateHandle[SEASON_ARG])

    private val _uiState: MutableStateFlow<GamesUiState> = MutableStateFlow(GamesUiState())
    override val uiState: StateFlow<GamesUiState> = _uiState

    override fun onEvent(event: GamesUiEvent) {
        when (event) {
            else -> {}
        }
    }

    init {
        getGamesResults(date = Date())
    }

    private fun getGamesResults(date: Date) {
        viewModelScope.launch {
            _uiState.update { it.copy(dataState = DataState.Loading) }
            getGamesResultsUseCase(
                leagueId = leagueId,
                season = season,
                date = date.toStringDate(SERVER_DATE_PATTERN)
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
                onFailure { response ->
                    Log.e("TAG", response.errorMessage.orEmpty())
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = { getGamesResults(date) }
                            )
                        )
                    }
                }
            }
        }
    }
}