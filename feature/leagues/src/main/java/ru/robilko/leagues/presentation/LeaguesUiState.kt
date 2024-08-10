package ru.robilko.leagues.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.League

data class LeaguesUiState(
    val dataState: DataState,
    val leagues: PersistentList<League> = persistentListOf(),
    val favouriteLeaguesIds: PersistentList<Int> = persistentListOf(),
    val selectedLeague: League? = null
) : UiState