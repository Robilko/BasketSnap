package ru.robilko.settings.domain.model

data class AppConfigData(
    val darkThemeConfig: DarkThemeConfig,
    val needToShowTopBar: Boolean,
    val enableImageBackground: Boolean
)