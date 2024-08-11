package ru.robilko.teams.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.TeamInfo

sealed class TeamsUiEvent : UiEvent {
    data class StarIconClick(val team: TeamInfo, val favourite: Boolean) : TeamsUiEvent()
}