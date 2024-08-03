package ru.robilko.home.presentation

import ru.robilko.core_ui.presentation.UiEvent

sealed class HomeUiEvent : UiEvent {
    data class OnTextChange(val searchQuery: String) : HomeUiEvent()
    data object RefreshCountries : HomeUiEvent()
}