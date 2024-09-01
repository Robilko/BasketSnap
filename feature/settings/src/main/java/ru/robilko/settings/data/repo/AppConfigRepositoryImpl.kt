package ru.robilko.settings.data.repo

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.robilko.local.di.SharedPreferencesDefault
import ru.robilko.settings.domain.model.AppConfigData
import ru.robilko.settings.domain.model.DarkThemeConfig
import ru.robilko.settings.domain.repo.AppConfigRepository
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    @SharedPreferencesDefault private val preferences: SharedPreferences
) : AppConfigRepository {

    private val appConfigData: MutableStateFlow<AppConfigData> = MutableStateFlow(
        AppConfigData(
            darkThemeConfig = getDarkThemeConfig()
        )
    )

    override fun getSettingsData(): StateFlow<AppConfigData> = appConfigData

    private fun getDarkThemeConfig(): DarkThemeConfig {
        val currentName =
            preferences.getString(DARK_THEME_CONFIG_KEY, DarkThemeConfig.FOLLOW_SYSTEM.name)
        return currentName.let {
            DarkThemeConfig.entries.firstOrNull { it.name == currentName }
                ?: DarkThemeConfig.FOLLOW_SYSTEM
        }
    }

    override fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        preferences.edit().putString(DARK_THEME_CONFIG_KEY, darkThemeConfig.name).apply()
        appConfigData.update { it.copy(darkThemeConfig = darkThemeConfig) }
    }

    private companion object {
        const val DARK_THEME_CONFIG_KEY = "dark_theme_config_key"
    }
}