package ru.robilko.favourites.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.League
import ru.robilko.model.data.TeamInfo

data class FavouritesUiState(
    val isLeaguesLoading: Boolean = false,
    val leagues: PersistentList<League> = persistentListOf(),
    val isTeamsLoading: Boolean = false,
    val teams: PersistentList<TeamInfo> = persistentListOf()
) : UiState
