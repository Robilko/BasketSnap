package ru.robilko.league_details.presentation

import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.League

data class LeagueDetailsUiState(
    val dataState: DataState,
    val league: League? = null
) : UiState
