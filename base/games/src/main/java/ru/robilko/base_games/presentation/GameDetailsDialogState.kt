package ru.robilko.base_games.presentation

import androidx.compose.runtime.Immutable
import ru.robilko.model.data.GameResults

@Immutable
sealed class GameDetailsDialogState {
    data object None : GameDetailsDialogState()
    data class ShowData(val gameResults: GameResults) : GameDetailsDialogState()
}