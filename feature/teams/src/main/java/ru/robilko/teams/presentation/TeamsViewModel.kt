package ru.robilko.teams.presentation

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base_favourites.domain.useCases.AddTeamToFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.DeleteTeamFromFavouritesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteTeamsUseCase
import ru.robilko.core_ui.R
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.model.data.TeamInfo
import ru.robilko.teams.domain.useCases.GetTeamsInfoUseCase
import ru.robilko.teams.navigation.LEAGUE_ID_ARG
import ru.robilko.teams.navigation.LEAGUE_NAME_ARG
import ru.robilko.teams.navigation.SEASON_ARG
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getTeamsInfoUseCase: GetTeamsInfoUseCase,
    private val addTeamToFavouritesUseCase: AddTeamToFavouritesUseCase,
    private val deleteTeamFromFavouritesUseCase: DeleteTeamFromFavouritesUseCase,
    private val getFavouriteTeamsUseCase: GetFavouriteTeamsUseCase
) : BaseAppViewModel<TeamsUiState, TeamsUiEvent>() {
    private val leagueId = checkNotNull<Int>(savedStateHandle[LEAGUE_ID_ARG])
    private val leagueName = checkNotNull<String>(savedStateHandle[LEAGUE_NAME_ARG])
    private val season = checkNotNull<String>(savedStateHandle[SEASON_ARG])

    private val _uiState: MutableStateFlow<TeamsUiState> = MutableStateFlow(TeamsUiState())
    override val uiState: StateFlow<TeamsUiState> = _uiState

    override fun onEvent(event: TeamsUiEvent) {
        when (event) {
            is TeamsUiEvent.StarIconClick ->
                makeActionOnStarIconClick(team = event.team, isFavourite = event.favourite)
        }
    }

    init {
        observeFavouriteTeams()
        getTeams(leagueId, season)
    }

    private fun observeFavouriteTeams() {
        viewModelScope.launch {
            getFavouriteTeamsUseCase().collectLatest { response ->
                if (response is Response.Success) {
                    val ids = response.data.filter { it.leagueId == leagueId }.map { it.id }
                        .toPersistentList()
                    _uiState.update { it.copy(favouriteTeamsIds = ids) }
                }
            }
        }
    }

    private fun getTeams(leagueId: Int, season: String) {
        viewModelScope.launch {
            getTeamsInfoUseCase(
                leagueId = leagueId,
                leagueName = leagueName,
                season = season
            ).apply {
                onFailure {
                    _uiState.update {
                        it.copy(dataState = DataState.Error(
                            message = context.getString(
                                R.string.getting_data_error
                            ),
                            onRetryAction = { getTeams(leagueId = leagueId, season = season) }
                        ))
                    }
                }
                onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            teams = response.data.toPersistentList(),
                            dataState = DataState.Success
                        )
                    }
                }
            }
        }
    }

    private fun makeActionOnStarIconClick(team: TeamInfo, isFavourite: Boolean) {
        viewModelScope.launch {
            if (isFavourite) deleteTeamFromFavouritesUseCase(team.id, team.leagueId)
            else addTeamToFavouritesUseCase(team)
        }
    }
}