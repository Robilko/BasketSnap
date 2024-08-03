package ru.robilko.leagues.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.League
import ru.robilko.model.data.Season

sealed class LeaguesUiEvent : UiEvent {
    data object DismissDetailsDialog : LeaguesUiEvent()
    data class LeagueCardClick(val league: League) : LeaguesUiEvent()
    data class SeasonClick(val season: Season, val league: League) : LeaguesUiEvent()
    data class StarIconClick(val league: League, val isFavourite: Boolean) : LeaguesUiEvent()
}