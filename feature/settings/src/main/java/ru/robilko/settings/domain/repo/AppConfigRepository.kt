package ru.robilko.settings.domain.repo

import kotlinx.coroutines.flow.StateFlow
import ru.robilko.settings.domain.model.AppConfigData
import ru.robilko.settings.domain.model.DarkThemeConfig

interface AppConfigRepository {
    fun getSettingsData(): StateFlow<AppConfigData>
    fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
    fun setShowTopBar(value: Boolean)
}