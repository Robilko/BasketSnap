package ru.robilko.model.data

data class TeamStatistics(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val country: Country,
    val league: LeagueShortInfo,
    val games: GamesInfo,
    val points: Points
)

data class LeagueShortInfo(
    val id: Int,
    val name: String,
    val type: String,
    val logoUrl: String,
)

data class GamesInfo(
    val played: CountByPlaceOfPlay,
    val wins: GamesResults,
    val draws: GamesResults,
    val loses: GamesResults
)

data class CountByPlaceOfPlay(
    val home: Int,
    val away: Int,
    val all: Int
)

data class GamesResults(
    val home: TotalResult,
    val away: TotalResult,
    val all: TotalResult
)

data class TotalResult(
    val total: Int,
    val percentage: String
)

data class PercentByPlaceOfPlay(
    val home: String,
    val away: String,
    val all: String
)

data class Points(
    val forTeam: PointsStatistics,
    val againstTeam: PointsStatistics
)

data class PointsStatistics(
    val total: CountByPlaceOfPlay,
    val average: PercentByPlaceOfPlay
)
