package ru.robilko.team_details.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.TeamStatistics

data class TeamDetailsUiState(
    val dataState: DataState = DataState.Loading,
    val seasons: PersistentList<Selectable> = persistentListOf(),
    val selectedSeason: Selectable? = null,
    val isFavourite: Boolean = false,
    val isLoadingStatistics: Boolean = false,
    val teamStatistics: TeamStatistics? = null,
    val showStatistics: Boolean = false,
    val showDraws: Boolean = true
) : UiState
