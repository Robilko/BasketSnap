package ru.robilko.home.presentation

import androidx.annotation.StringRes
import ru.robilko.home.R

enum class HomeTabs (@StringRes val titleResId: Int) {
    TODAY(R.string.today_tab_title),
    SEARCH(R.string.search_tab_title)
}