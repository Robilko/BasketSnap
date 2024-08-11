package ru.robilko.league_details.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.Season

sealed class LeagueDetailsUiEvent : UiEvent {
    data object DismissSeasonDialog : LeagueDetailsUiEvent()
    data object StarIconClick : LeagueDetailsUiEvent()
    data class SeasonClick(val season: Season) : LeagueDetailsUiEvent()
}