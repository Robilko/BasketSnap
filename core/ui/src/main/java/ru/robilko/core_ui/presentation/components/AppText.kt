package ru.robilko.core_ui.presentation.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import ru.robilko.core_ui.theme.BasketSnapTheme

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BasketSnapTheme.colors.primaryText,
    style: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    fontFamily: FontFamily? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    textDecoration: TextDecoration? = null
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        textAlign = textAlign,
        fontFamily = fontFamily,
        lineHeight = lineHeight,
        maxLines = maxLines,
        textDecoration = textDecoration
    )
}