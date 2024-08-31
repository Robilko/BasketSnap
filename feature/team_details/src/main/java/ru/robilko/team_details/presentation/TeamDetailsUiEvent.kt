package ru.robilko.team_details.presentation

import ru.robilko.core_ui.presentation.UiEvent

sealed class TeamDetailsUiEvent : UiEvent {
    data object StarIconClick : TeamDetailsUiEvent()
    data class SeasonClick(val season: String) : TeamDetailsUiEvent()
}