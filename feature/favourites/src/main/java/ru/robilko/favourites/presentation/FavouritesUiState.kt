package ru.robilko.favourites.presentation

import ru.robilko.core_ui.presentation.UiState

data class FavouritesUiState(
    val isLoading: Boolean = false
) : UiState
