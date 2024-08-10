package ru.robilko.league_details.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.league_details.domain.useCases.GetLeagueUseCase
import ru.robilko.league_details.navigation.LEAGUE_ID_ARG
import javax.inject.Inject
import ru.robilko.core_ui.R as R_core_ui

@HiltViewModel
class LeagueDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getLeagueUseCase: GetLeagueUseCase
) : BaseAppViewModel<LeagueDetailsUiState, LeagueDetailsUiEvent>() {
    private val _uiState: MutableStateFlow<LeagueDetailsUiState> =
        MutableStateFlow(LeagueDetailsUiState(DataState.Loading))
    override val uiState: StateFlow<LeagueDetailsUiState> = _uiState
    private val leagueId = checkNotNull<String>(savedStateHandle[LEAGUE_ID_ARG]).toInt()

    override fun onEvent(event: LeagueDetailsUiEvent) {
        when (event) {
            is LeagueDetailsUiEvent.SeasonClick -> {}
        }
    }

    init {
        initLeague()
    }

    private fun initLeague() {
        viewModelScope.launch {
            getLeagueUseCase(leagueId).apply {
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success,
                            league = response.data
                        )
                    }
                }

                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R_core_ui.string.getting_data_error
                                ),
                                onRetryAction = ::initLeague
                            )
                        )
                    }
                }
            }
        }
    }
}