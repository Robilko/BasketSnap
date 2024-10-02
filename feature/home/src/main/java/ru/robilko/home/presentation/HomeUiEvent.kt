package ru.robilko.home.presentation

import ru.robilko.core_ui.presentation.UiEvent
import ru.robilko.model.data.GameResults

sealed class HomeUiEvent : UiEvent {
    data class OnTextChange(val searchQuery: String) : HomeUiEvent()
    data class GameCardClick(val game: GameResults) : HomeUiEvent()
    data class ChangeCheckedFavourites(val value: Boolean) : HomeUiEvent()
    data object OnScreenEntered : HomeUiEvent()
    data object OnScreenExited : HomeUiEvent()
    data object DetailsDialogDismiss : HomeUiEvent()
}