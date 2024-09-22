package ru.robilko.settings.presentation

import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiEvent

sealed class SettingsUiEvent : UiEvent {
    data object ClickAppThemeSetting : SettingsUiEvent()
    data object DismissSelectableDialog : SettingsUiEvent()
    data class SelectNewChoice(val selectable: Selectable) : SettingsUiEvent()
    data class CheckedChangeShowTopBar(val value: Boolean) : SettingsUiEvent()
    data class CheckedChangeEnableImageBackground(val value: Boolean) : SettingsUiEvent()
}