package ru.robilko.league_details.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base_favourites.domain.useCases.AddLeagueToFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.DeleteLeagueFromFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteLeaguesUseCase
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
    private val getLeagueUseCase: GetLeagueUseCase,
    private val getFavouriteLeaguesUseCase: GetFavouriteLeaguesUseCase,
    private val addLeagueToFavouritesUseCase: AddLeagueToFavouritesUseCase,
    private val deleteLeagueFromFavouritesUseCase: DeleteLeagueFromFavouritesUseCase
) : BaseAppViewModel<LeagueDetailsUiState, LeagueDetailsUiEvent>() {
    private val leagueId = checkNotNull<Int>(savedStateHandle[LEAGUE_ID_ARG])

    private val _uiState: MutableStateFlow<LeagueDetailsUiState> =
        MutableStateFlow(LeagueDetailsUiState(DataState.Loading))
    override val uiState: StateFlow<LeagueDetailsUiState> = _uiState

    override fun onEvent(event: LeagueDetailsUiEvent) {
        when (event) {
            is LeagueDetailsUiEvent.SeasonClick -> _uiState.update { it.copy(selectedSeason = event.season.season) }
            LeagueDetailsUiEvent.DismissSeasonDialog -> _uiState.update { it.copy(selectedSeason = null) }
            LeagueDetailsUiEvent.StarIconClick -> makeActionOnStarIconClick()
        }
    }

    init {
        initLeague()
        observeFavouriteLeagues()
    }

    private fun initLeague() {
        viewModelScope.launch {
            getLeagueUseCase(leagueId).apply {
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Success, league = response.data
                        )
                    }
                }

                onFailure {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                message = context.getString(
                                    R_core_ui.string.getting_data_error
                                ), onRetryAction = ::initLeague
                            )
                        )
                    }
                }
            }
        }
    }

    private fun observeFavouriteLeagues() {
        viewModelScope.launch {
            getFavouriteLeaguesUseCase().collectLatest { response ->
                if (response is Response.Success) {
                    _uiState.update { it.copy(isFavourite = response.data.any { league -> league.id == leagueId }) }
                }
            }
        }
    }

    private fun makeActionOnStarIconClick() {
        viewModelScope.launch {
            if (_uiState.value.isFavourite) deleteLeagueFromFavouritesUseCase(leagueId)
            else _uiState.value.league?.let { addLeagueToFavouritesUseCase(it) }
        }
    }
}