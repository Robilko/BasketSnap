package ru.robilko.team_details.presentation

import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.GameResults

sealed class TeamDetailsUiEvent : UiEvent {
    data object StarIconClick : TeamDetailsUiEvent()
    data object DetailsDialogDismiss : TeamDetailsUiEvent()
    data class SeasonClick(val season: Selectable) : TeamDetailsUiEvent()
    data class GameCardClick(val gameResults: GameResults) : TeamDetailsUiEvent()
}