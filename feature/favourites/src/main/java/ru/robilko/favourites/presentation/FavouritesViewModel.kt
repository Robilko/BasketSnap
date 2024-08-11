package ru.robilko.favourites.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base_favourites.domain.useCases.DeleteLeagueFromFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.DeleteTeamFromFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteLeaguesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteTeamsUseCase
import ru.robilko.core_ui.presentation.BaseAppViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteLeaguesUseCase: GetFavouriteLeaguesUseCase,
    private val deleteLeagueFromFavouritesUseCase: DeleteLeagueFromFavouritesUseCase,
    private val getFavouriteTeamsUseCase: GetFavouriteTeamsUseCase,
    private val deleteTeamFromFavouritesUseCase: DeleteTeamFromFavouritesUseCase
) : BaseAppViewModel<FavouritesUiState, FavouritesUiEvent>() {

    private val _uiState: MutableStateFlow<FavouritesUiState> =
        MutableStateFlow(FavouritesUiState())
    override val uiState: StateFlow<FavouritesUiState> = _uiState

    override fun onEvent(event: FavouritesUiEvent) {
        when (event) {
            is FavouritesUiEvent.DeleteLeagueIconClick ->
                viewModelScope.launch { deleteLeagueFromFavouritesUseCase(event.league.id) }

            is FavouritesUiEvent.DeleteTeamIconClick ->
                viewModelScope.launch { deleteTeamFromFavouritesUseCase(event.teamInfo.id) }
        }
    }

    init {
        observeOnFavouriteLeagues()
        observeOnFavouriteTeams()
    }

    private fun observeOnFavouriteLeagues() {
        viewModelScope.launch {
            getFavouriteLeaguesUseCase()
                .onStart { _uiState.update { it.copy(isLeaguesLoading = true) } }
                .collectLatest { response ->
                    response.apply {
                        onSuccess {
                            it.data.toPersistentList().let { leagues ->
                                _uiState.update { state ->
                                    state.copy(
                                        isLeaguesLoading = false,
                                        leagues = leagues
                                    )
                                }
                            }
                        }
                        onFailure { _uiState.update { it.copy(isLeaguesLoading = false) } }
                    }
                }
        }
    }

    private fun observeOnFavouriteTeams() {
        viewModelScope.launch {
            getFavouriteTeamsUseCase()
                .onStart { _uiState.update { it.copy(isTeamsLoading = true) } }
                .collectLatest { response ->
                    response.apply {
                        onSuccess {
                            it.data.toPersistentList().let { teamInfos ->
                                _uiState.update { state ->
                                    state.copy(
                                        isTeamsLoading = false,
                                        teams = teamInfos
                                    )
                                }
                            }
                        }
                        onFailure { _uiState.update { it.copy(isTeamsLoading = false) } }
                    }
                }
        }
    }
}