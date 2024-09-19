package ru.robilko.settings.presentation

import androidx.annotation.StringRes
import kotlinx.collections.immutable.PersistentList
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.UiState

data class SettingsUiState(
    val darkThemeConfig: Selectable? = null,
    val needToShowTopBar: Boolean? = null,
    val selectableDialogState: SelectableDialogState? = null,
    val appVersionName: String
) : UiState

sealed class SelectableDialogState(open val data: SelectableDialogData) {
    data class ShowThemeChoices(override val data: SelectableDialogData) :
        SelectableDialogState(data)

    data class ShowLanguageChoices(override val data: SelectableDialogData) :
        SelectableDialogState(data)
}

data class SelectableDialogData(
    @StringRes val titleResId: Int,
    val selected: Selectable?,
    val choices: PersistentList<Selectable>
)