package ru.robilko.home.presentation.home

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.Country

data class HomeUiState(
    val dataState: HomeDataState
) : UiState

sealed class HomeDataState {
    data object Loading : HomeDataState()
    data class Success(val countries: PersistentList<Country> = persistentListOf()) :
        HomeDataState()

    data class Error(val message: String, val onRetryAction: () -> Unit) : HomeDataState()
}
