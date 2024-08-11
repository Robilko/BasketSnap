package ru.robilko.favourites.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.League
import ru.robilko.model.data.TeamInfo

sealed class FavouritesUiEvent : UiEvent {
    data class DeleteLeagueIconClick(val league: League) : FavouritesUiEvent()
    data class DeleteTeamIconClick(val teamInfo: TeamInfo) : FavouritesUiEvent()
}