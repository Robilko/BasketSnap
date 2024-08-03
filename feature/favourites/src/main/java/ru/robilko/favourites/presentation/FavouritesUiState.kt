package ru.robilko.favourites.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.League

data class FavouritesUiState(
    val isLeaguesLoading: Boolean = false,
    val leagues: PersistentList<League> = persistentListOf(),
    val isTeamsLoading: Boolean = false,
    val teams: PersistentList<String> = persistentListOf()
) : UiState
