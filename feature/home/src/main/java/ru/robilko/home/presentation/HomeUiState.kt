package ru.robilko.home.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.Country

data class HomeUiState(
    val dataState: HomeDataState,
    val continents: PersistentList<Country> = persistentListOf(),
    val countries: PersistentList<Country> = persistentListOf(),
    val searchQuery: String = ""
) : UiState

sealed class HomeDataState {
    data object Loading : HomeDataState()
    data object Success : HomeDataState()
    data class Error(val message: String, val onRetryAction: () -> Unit) : HomeDataState()
}
