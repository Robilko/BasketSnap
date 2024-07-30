package ru.robilko.home.presentation.home

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.Country

sealed class HomeUiEvent : UiEvent {
    data class ClickCountry(val country: Country) : HomeUiEvent()
}