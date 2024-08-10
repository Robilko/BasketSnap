package ru.robilko.leagues.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.League

sealed class LeaguesUiEvent : UiEvent {
    data class StarIconClick(val league: League, val isFavourite: Boolean) : LeaguesUiEvent()
}