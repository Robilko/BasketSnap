package ru.robilko.settings.presentation

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.SelectableData
import ru.robilko.settings.R
import ru.robilko.settings.domain.model.DarkThemeConfig
import ru.robilko.settings.domain.repo.AppConfigRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val appConfigRepository: AppConfigRepository
) : BaseAppViewModel<SettingsUiState, SettingsUiEvent>() {
    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    override val uiState: StateFlow<SettingsUiState> = _uiState

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.ClickAppThemeSetting -> showSelectThemeDialog()

            SettingsUiEvent.DismissSelectableDialog ->
                _uiState.update { it.copy(selectableDialogState = null) }

            is SettingsUiEvent.SelectNewChoice -> onNewChoiceSelected(event.selectable)
            is SettingsUiEvent.CheckedChangeShowTopBar -> changeShowTopBarConfig(event.value)
        }
    }

    init {
        observeAppConfigData()
    }

    private fun observeAppConfigData() {
        viewModelScope.launch {
            appConfigRepository.getSettingsData().collectLatest { data ->
                _uiState.update {
                    it.copy(
                        darkThemeConfig = data.darkThemeConfig.asSelectable(),
                        needToShowTopBar = data.needToShowTopBar
                    )
                }
            }
        }
    }

    private fun showSelectThemeDialog() {
        _uiState.update {
            it.copy(
                selectableDialogState = SelectableDialogState.ShowThemeChoices(
                    data = SelectableDialogData(
                        titleResId = R.string.change_theme_setting_title,
                        selected = _uiState.value.darkThemeConfig,
                        choices = getThemeChoices()
                    )
                )
            )
        }
    }

    private fun DarkThemeConfig.asSelectable(): Selectable =
        SelectableData(
            value = name,
            name = appContext.getString(titleResId)
        )

    private fun getThemeChoices(): PersistentList<Selectable> =
        DarkThemeConfig.entries.map { it.asSelectable() }.toPersistentList()

    private fun onNewChoiceSelected(selectable: Selectable) {
        when (_uiState.value.selectableDialogState) {
            is SelectableDialogState.ShowThemeChoices -> {
                val newChoice =
                    DarkThemeConfig.entries.firstOrNull { it.name == selectable.value } ?: return
                appConfigRepository.setDarkThemeConfig(newChoice)
            }

            is SelectableDialogState.ShowLanguageChoices -> {
                //todo реализовать выбор языка
            }

            null -> {}
        }
        _uiState.update { it.copy(selectableDialogState = null) }
    }

    private fun changeShowTopBarConfig(value: Boolean) {
        if (_uiState.value.needToShowTopBar == value) return
        appConfigRepository.setShowTopBar(value)
    }
}