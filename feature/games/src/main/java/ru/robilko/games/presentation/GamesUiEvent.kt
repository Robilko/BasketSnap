package ru.robilko.games.presentation

import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.GameResults

sealed class GamesUiEvent : UiEvent {
    data class SeasonClick(val season: Selectable) : GamesUiEvent()
    data class GameCardClick(val gameResults: GameResults) : GamesUiEvent()
    data object DetailsDialogDismiss : GamesUiEvent()
}