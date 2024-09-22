package ru.robilko.team_details.presentation

import androidx.annotation.StringRes
import ru.robilko.team_details.R

enum class TeamDetailsTabs(@StringRes val titleResId: Int) {
    GAMES(R.string.games_tab_title),
    STATISTICS(R.string.statistics_tab_title)
}