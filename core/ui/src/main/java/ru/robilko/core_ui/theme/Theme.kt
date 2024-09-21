package ru.robilko.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Orange300,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    error = Red900,
    background = Color(0xFF373737),
    onPrimary = Color(0xFF373737)
)

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    error = Red900,
    background = Color.White,
    onPrimary = Color.White

    /* Other default colors to override

    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

data class BasketSnapColors(
    val primaryText: Color,
    val secondaryText: Color,
    val favouriteIcon: Color = Yellow700
)

private val LightColorPalette = BasketSnapColors(
    primaryText = Color.Black,
    secondaryText = Color.Gray
)

private val DarkColorPalette = BasketSnapColors(
    primaryText = Color.White,
    secondaryText = Color.LightGray
)

@Composable
fun BasketSnapTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val colorPalette = if (isDarkTheme) DarkColorPalette else LightColorPalette

    CompositionLocalProvider(
        LocalBasketSnapColors provides colorPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object BasketSnapTheme {
    val colors: BasketSnapColors
        @Composable
        get() = LocalBasketSnapColors.current
}

internal val LocalBasketSnapColors =
    staticCompositionLocalOf<BasketSnapColors> { error("No colors provided") }

