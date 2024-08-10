package ru.robilko.favourites.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.League

sealed class FavouritesUiEvent : UiEvent {
    data class DeleteLeagueIconClick(val league: League) : FavouritesUiEvent()
    data class TeamCardClick(val team: String) : FavouritesUiEvent()
    data class DeleteTeamIconClick(val team: String) : FavouritesUiEvent()
}