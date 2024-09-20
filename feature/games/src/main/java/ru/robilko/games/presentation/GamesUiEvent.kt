package ru.robilko.games.presentation

import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiEvent

sealed class GamesUiEvent : UiEvent {
    data class SeasonClick(val season: Selectable) : GamesUiEvent()
}