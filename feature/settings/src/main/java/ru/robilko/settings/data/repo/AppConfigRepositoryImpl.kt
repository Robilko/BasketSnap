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
            darkThemeConfig = getDarkThemeConfig(),
            needToShowTopBar = preferences.getBoolean(SHOW_TOP_BAR_KEY, true),
            enableImageBackground = preferences.getBoolean(ENABLE_IMAGE_BACKGROUND_KEY, false)
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

    override fun setShowTopBar(value: Boolean) {
        preferences.edit().putBoolean(SHOW_TOP_BAR_KEY, value).apply()
        appConfigData.update { it.copy(needToShowTopBar = value) }
    }

    override fun setEnableImageBackground(value: Boolean) {
        preferences.edit().putBoolean(ENABLE_IMAGE_BACKGROUND_KEY, value).apply()
        appConfigData.update { it.copy(enableImageBackground = value) }
    }

    private companion object {
        const val DARK_THEME_CONFIG_KEY = "dark_theme_config_key"
        const val SHOW_TOP_BAR_KEY = "show_top_bar_key"
        const val ENABLE_IMAGE_BACKGROUND_KEY = "enable_image_background_key"
    }
}