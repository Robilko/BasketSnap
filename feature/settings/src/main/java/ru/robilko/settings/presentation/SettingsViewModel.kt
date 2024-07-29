package ru.robilko.settings.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.robilko.core_ui.presentation.BaseAppViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : BaseAppViewModel<SettingsUiState, SettingsUiEvent>() {
    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    override val uiState: StateFlow<SettingsUiState> = _uiState

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            else -> {}
        }
    }
}