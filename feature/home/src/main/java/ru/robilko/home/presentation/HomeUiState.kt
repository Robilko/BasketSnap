package ru.robilko.home.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.Country
import ru.robilko.model.data.GameResults

data class HomeUiState(
    val todayTabState: DataState = DataState.Loading,
    val searchTabState: DataState = DataState.Loading,
    val checkedFavourite: Boolean = true,
    val detailsDialogState: GameDetailsDialogState = GameDetailsDialogState.None,
    val todayGames: PersistentList<GameResults> = persistentListOf(),
    val continents: PersistentList<Country> = persistentListOf(),
    val countries: PersistentList<Country> = persistentListOf(),
    val searchQuery: String = ""
) : UiState
