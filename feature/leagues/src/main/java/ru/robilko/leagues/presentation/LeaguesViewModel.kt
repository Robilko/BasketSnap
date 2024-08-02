package ru.robilko.leagues.presentation

import android.content.Context
import android.widget.Toast
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
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.leagues.domain.useCases.GetLeaguesByCountryUseCase
import ru.robilko.leagues.navigation.COUNTRY_ID_ARG
import javax.inject.Inject
import ru.robilko.core_ui.R as R_core_ui

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getLeaguesByCountryUseCase: GetLeaguesByCountryUseCase
) : BaseAppViewModel<LeaguesUiState, LeaguesUiEvent>() {
    private val _uiState: MutableStateFlow<LeaguesUiState> =
        MutableStateFlow(LeaguesUiState(LeaguesDataState.Loading))
    override val uiState: StateFlow<LeaguesUiState> = _uiState
    private val countryId = checkNotNull<String>(savedStateHandle[COUNTRY_ID_ARG]).toInt()

    override fun onEvent(event: LeaguesUiEvent) {
        when (event) {
            is LeaguesUiEvent.LeagueCardClick -> _uiState.update { it.copy(selectedLeague = event.league) }
            LeaguesUiEvent.DismissDetailsDialog -> _uiState.update { it.copy(selectedLeague = null) }
            is LeaguesUiEvent.SeasonClick -> {
                //todo
                Toast.makeText(
                    context,
                    "${event.league.name}, season ${event.season.season}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is LeaguesUiEvent.StarIconClick -> {
                //todo
                Toast.makeText(
                    context,
                    "${event.league.name} add to favourite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    init {
        getLeagues(countryId)
    }

    private fun getLeagues(countryId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(dataState = LeaguesDataState.Loading) }
            getLeaguesByCountryUseCase(countryId).apply {
                onFailure {
                    _uiState.update {
                        it.copy(dataState = LeaguesDataState.Error(
                            message = context.getString(
                                R_core_ui.string.getting_data_error
                            ),
                            onRetryAction = { getLeagues(countryId) }
                        ))
                    }
                }
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            leagues = response.data.toPersistentList(),
                            dataState = LeaguesDataState.Leagues
                        )
                    }
                }
            }
        }
    }
}