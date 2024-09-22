package ru.robilko.model.data

import java.util.Date

data class GameResults(
    val id: Int,
    val date: Date?,
    val time: String,
    val timestamp: Long,
    val timezone: String,
    val venue: String,
    val statusLong: String,
    val statusShort: String,
    val timer: String?,
    val league: GameLeague,
    val country: Country,
    val homeTeam: GameTeamInfo,
    val awayTeam: GameTeamInfo,
    val homeScore: GameScore,
    val awayScore: GameScore,
    val isPlayingNow: Boolean
)

data class GameLeague(
    val id: Int,
    val name: String,
    val type: String,
    val season: String,
    val logoUrl: String,
)

data class GameTeamInfo(
    val id: Int,
    val name: String,
    val logoUrl: String
)

data class GameScore(
    val quarter1: Int?,
    val quarter2: Int?,
    val quarter3: Int?,
    val quarter4: Int?,
    val overTime: Int?,
    val total: Int?
)