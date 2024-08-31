package ru.robilko.teams.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.TeamInfo

data class TeamsUiState(
    val dataState: DataState = DataState.Loading,
    val teams: PersistentList<TeamInfo> = persistentListOf(),
    val favouriteTeamsIds: PersistentList<Int> = persistentListOf(),
) : UiState
