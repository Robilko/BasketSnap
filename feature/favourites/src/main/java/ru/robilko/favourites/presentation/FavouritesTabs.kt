package ru.robilko.favourites.presentation

import androidx.annotation.StringRes
import ru.robilko.favourites.R

enum class FavouritesTabs(@StringRes val titleResId: Int) {
    TEAMS(R.string.teams_tab_title),
    LEAGUES(R.string.leagues_tab_title)
}