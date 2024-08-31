package ru.robilko.core_ui.presentation.components

import AppOutlinedTextField
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.SelectableData
import ru.robilko.core_ui.theme.BasketSnapTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppSelectableOutlinedTextField(
    title: String,
    selected: Selectable?,
    choices: PersistentList<Selectable>,
    onSelectionChange: (Selectable) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "")

    Box {
        AppOutlinedTextField(
            value = selected?.name.orEmpty(),
            title = title,
            enabled = false,
            readOnly = true,
            modifier = modifier.clickable { isExpanded = !isExpanded },
            onChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = BasketSnapTheme.colors.primaryText,
                unfocusedTextColor = BasketSnapTheme.colors.primaryText,
                disabledTextColor = BasketSnapTheme.colors.primaryText,
                disabledBorderColor = BasketSnapTheme.colors.primaryText,
                errorTextColor = BasketSnapTheme.colors.primaryText,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                focusedBorderColor = BasketSnapTheme.colors.primaryText,
                unfocusedBorderColor = BasketSnapTheme.colors.primaryText,
                cursorColor = BasketSnapTheme.colors.primaryText
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = BasketSnapTheme.colors.primaryText,
                    modifier = Modifier
                        .graphicsLayer(rotationZ = rotationAngle)
                        .size(16.dp)
                )

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    scrollState = rememberScrollState(),
                    modifier = Modifier
                        .heightIn(max = 400.dp),
                    properties = PopupProperties(focusable = true,usePlatformDefaultWidth = true)
                ) {
                    choices.forEach { choice ->
                        DropdownMenuItem(
                            text = { AppText(text = choice.name) },
                            onClick = {
                                isExpanded = false
                                if (selected != choice) onSelectionChange(choice)
                            },
                            trailingIcon = {
                                if (selected == choice) Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = BasketSnapTheme.colors.primaryText
                                )
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun AppSelectableOutlinedTextFieldPreview() {
    BasketSnapTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AppSelectableOutlinedTextField(
                title = "Заголовок",
                selected = SelectableData("Выбор 1", "Выбор 1"),
                choices = persistentListOf(
                    SelectableData("Выбор 1", "Выбор 1"),
                    SelectableData("Выбор 2", "Выбор 2"),
                    SelectableData("Выбор 3", "Выбор 3")
                ),
                onSelectionChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}