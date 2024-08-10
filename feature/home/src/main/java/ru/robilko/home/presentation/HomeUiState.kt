package ru.robilko.home.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.Country

data class HomeUiState(
    val dataState: DataState,
    val continents: PersistentList<Country> = persistentListOf(),
    val countries: PersistentList<Country> = persistentListOf(),
    val searchQuery: String = ""
) : UiState
