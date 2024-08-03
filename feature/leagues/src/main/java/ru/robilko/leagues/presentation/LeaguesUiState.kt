package ru.robilko.leagues.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.League

data class LeaguesUiState(
    val dataState: LeaguesDataState,
    val leagues: PersistentList<League> = persistentListOf(),
    val favouriteLeaguesIds: PersistentList<Int> = persistentListOf(),
    val selectedLeague: League? = null
) : UiState

sealed class LeaguesDataState {
    data object Loading : LeaguesDataState()
    data object Leagues : LeaguesDataState()
    data class Error(val message: String, val onRetryAction: () -> Unit) : LeaguesDataState()
}