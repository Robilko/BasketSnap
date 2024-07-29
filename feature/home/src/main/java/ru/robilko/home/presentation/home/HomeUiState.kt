package ru.robilko.home.presentation.home

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.UiState
import ru.robilko.model.data.Country

data class HomeUiState(
    val isLoading: Boolean = false,
    val countries: PersistentList<ru.robilko.model.data.Country> = persistentListOf()
) : UiState
