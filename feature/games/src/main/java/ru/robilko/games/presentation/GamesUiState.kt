package ru.robilko.games.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.GameResults

data class GamesUiState(
    val dataState: DataState = DataState.Loading,
    val gameResults: PersistentList<GameResults> = persistentListOf(),
    val showSeasons: Boolean = true,
    val seasons: PersistentList<Selectable> = persistentListOf(),
    val selectedSeason: Selectable? = null,
    val detailsDialogState: GameDetailsDialogState = GameDetailsDialogState.None
) : UiState