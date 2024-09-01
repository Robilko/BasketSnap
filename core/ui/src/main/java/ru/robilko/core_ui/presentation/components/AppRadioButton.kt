package ru.robilko.core_ui.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.annotations.TestOnly
import ru.robilko.core_ui.presentation.Selectable
import ru.robilko.core_ui.presentation.SelectableData
import ru.robilko.core_ui.theme.BasketSnapTheme

private val radioButtonSize = 24.dp

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.primary,
        unselectedColor = Color.Gray
    ),
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier.size(radioButtonSize),
        enabled = enabled,
        colors = colors
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewAppRadioButton() {
    BasketSnapTheme(false) {
        Column(modifier = Modifier.fillMaxSize(0.5f)) {
            Row {
                AppRadioButton(selected = true, onClick = {})
                AppRadioButton(selected = false, onClick = {})
            }
            Row {
                AppRadioButton(selected = true, onClick = {}, enabled = false)
                AppRadioButton(selected = false, onClick = {}, enabled = false)
            }
        }
    }
}

@Composable
fun AppRadioGroup(
    selected: Selectable?,
    choices: List<Selectable>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textStyle: TextStyle = LocalTextStyle.current,
    textSize: TextUnit = TextUnit.Unspecified,
    onSelect: (Selectable) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        choices.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppRadioButton(
                    selected = item.value == selected?.value,
                    onClick = { if (item.value != selected?.value) onSelect(item) }
                )

                AppText(
                    text = item.name,
                    style = textStyle,
                    fontSize = textSize,
                    modifier = Modifier.clickable { if (item.value != selected?.value) onSelect(item) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun PreviewAppRadioGroup() {
    BasketSnapTheme(false) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppRadioGroup(
                selected = mockChoices.first(),
                choices = mockChoices
            ) {}
        }
    }
}

@TestOnly
private val mockChoices = listOf<Selectable>(
    SelectableData("1", "Вариант 1"),
    SelectableData("2", "Вариант 2"),
    SelectableData("3", "Вариант 3")
)