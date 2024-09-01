package ru.robilko.settings.domain.model

import androidx.annotation.StringRes
import ru.robilko.settings.R

enum class DarkThemeConfig(@StringRes val titleResId: Int) {
    FOLLOW_SYSTEM(R.string.follow_system_theme_title),
    LIGHT(R.string.light_theme_title),
    DARK(R.string.dark_theme_title)
}