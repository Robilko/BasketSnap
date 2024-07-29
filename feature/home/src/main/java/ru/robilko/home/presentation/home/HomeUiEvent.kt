package ru.robilko.home.presentation.home

import ru.robilko.core_ui.presentation.UiEvent

sealed class HomeUiEvent : UiEvent {
    data object ClickCountriesButton : HomeUiEvent()
}