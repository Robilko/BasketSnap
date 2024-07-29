package ru.robilko.core_ui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseAppViewModel<S:UiState, E: UiEvent>() : ViewModel() {
    abstract val uiState: StateFlow<S>
    abstract fun onEvent(event: E)
}

interface UiState
interface UiEvent