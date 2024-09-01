package ru.robilko.settings.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.PersistentList
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.SelectableData
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppRadioGroup
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.settings.R
import ru.robilko.settings.domain.model.DarkThemeConfig

@Composable
internal fun SettingsRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.settings_title) }
    SettingsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    uiState.selectableDialogState?.data?.let { dialogData ->
        SettingsSelectableDialog(
            titleResId = dialogData.titleResId,
            selected = dialogData.selected,
            choices = dialogData.choices,
            onSelect = { onEvent(SettingsUiEvent.SelectNewChoice(it)) },
            onDismiss = { onEvent(SettingsUiEvent.DismissSelectableDialog) }
        )
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SettingsItem(
            iconResId = R.drawable.ic_theme_mode,
            text = stringResource(R.string.change_theme_setting_title),
            onClick = { onEvent(SettingsUiEvent.ClickAppThemeSetting) }
        )
    }
}

@Composable
private fun SettingsItem(@DrawableRes iconResId: Int, text: String, onClick: () -> Unit) {
    AppCard(modifier = Modifier.bounceClick { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconResId),
                contentDescription = null,
                tint = BasketSnapTheme.colors.primaryText
            )
            AppText(
                text = text,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = BasketSnapTheme.colors.primaryText
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSelectableDialog(
    @StringRes titleResId: Int,
    selected: Selectable?,
    choices: PersistentList<Selectable>,
    onSelect: (Selectable) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        AppCard {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppText(
                    text = stringResource(id = titleResId),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                AppRadioGroup(
                    selected = selected,
                    choices = choices,
                    modifier = Modifier.fillMaxWidth(),
                    onSelect = onSelect
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SettingsPreview() {
    BasketSnapTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                darkThemeConfig = SelectableData(
                    DarkThemeConfig.FOLLOW_SYSTEM.name,
                    "Как в системе"
                )
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}