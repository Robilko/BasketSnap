package ru.robilko.model.data

import androidx.annotation.StringRes
import ru.robilko.model.R

enum class GameStatus(@StringRes val titleResId: Int) {
    NS(R.string.not_started_status),
    Q1(R.string.quarter_1_status),
    Q2(R.string.quarter_2_status),
    Q3(R.string.quarter_3_status),
    Q4(R.string.quarter_4_status),
    OT(R.string.over_time_status),
    BT(R.string.break_time_status),
    HT(R.string.halftime_status),
    FT(R.string.game_finished_status),
    AOT(R.string.after_over_time_status),
    POST(R.string.game_postponed_status),
    CANC (R.string.game_cancelled_status),
    SUSP (R.string.game_suspended_status),
    AWD (R.string.game_awarded_status),
    ABD (R.string.game_abandoned_status),
    UNKNOWN(R.string.unknown_game_status);

    companion object {
        fun GameStatus.isPlayingNow():Boolean = this in listOf(Q1, Q2, Q3, Q4, OT, BT, HT)
    }
}
