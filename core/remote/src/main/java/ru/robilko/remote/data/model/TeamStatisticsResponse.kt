package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.model.data.CountByPlaceOfPlay
import ru.robilko.model.data.GamesInfo
import ru.robilko.model.data.GamesResults
import ru.robilko.model.data.LeagueShortInfo
import ru.robilko.model.data.PercentByPlaceOfPlay
import ru.robilko.model.data.Points
import ru.robilko.model.data.PointsStatistics
import ru.robilko.model.data.TeamStatistics
import ru.robilko.model.data.TotalResult
import java.util.Locale

@Serializable
data class TeamStatisticsResponse(
    @SerialName("country") val country: CountryDto,
    @SerialName("league") val league: LeagueShortInfoDto,
    @SerialName("team") val team: TeamDto,
    @SerialName("games") val games: GamesInfoDto,
    @SerialName("points") val points: PointsDto
)

fun TeamStatisticsResponse.asDomainModel() = TeamStatistics(
    id = team.id,
    name = team.name,
    logoUrl = team.logoUrl,
    country = country.asDomainModel(),
    league = league.asDomainModel(),
    games = games.asDomainModel(),
    points = points.asDomainModel()
)

@Serializable
data class LeagueShortInfoDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("logo") val logoUrl: String
)

fun LeagueShortInfoDto.asDomainModel() = LeagueShortInfo(
    id = id,
    name = name,
    type = type,
    logoUrl = logoUrl
)

@Serializable
data class GamesInfoDto(
    @SerialName("played") val played: CountByPlaceOfPlayDto,
    @SerialName("wins") val wins: GamesResultsDto,
    @SerialName("draws") val draws: GamesResultsDto,
    @SerialName("loses") val loses: GamesResultsDto
)

fun GamesInfoDto.asDomainModel() = GamesInfo(
    played = played.asDomainModel(),
    wins = wins.asDomainModel(),
    draws = draws.asDomainModel(),
    loses = loses.asDomainModel()
)

@Serializable
data class CountByPlaceOfPlayDto(
    @SerialName("home") val home: Int,
    @SerialName("away") val away: Int,
    @SerialName("all") val all: Int
)

fun CountByPlaceOfPlayDto.asDomainModel() = CountByPlaceOfPlay(
    home = home,
    away = away,
    all = all
)

@Serializable
data class GamesResultsDto(
    @SerialName("home") val home: TotalResultDto,
    @SerialName("away") val away: TotalResultDto,
    @SerialName("all") val all: TotalResultDto
)

fun GamesResultsDto.asDomainModel() = GamesResults(
    home = home.asDomainModel(),
    away = away.asDomainModel(),
    all = all.asDomainModel()
)

@Serializable
data class TotalResultDto(
    @SerialName("total") val total: Int,
    @SerialName("percentage") val percentage: String?
)

fun TotalResultDto.asDomainModel() = TotalResult(
    total = total,
    percentage = getFormattedPercentage(percentage)
)

private fun getFormattedPercentage(percentage: String?): String =
    percentage?.toDoubleOrNull()?.let { percent ->
        String.format(Locale.ROOT, "%.1f", percent * 100).let {
            if (it.endsWith(".0")) it.dropLast(2) else it
        }
    } ?: "0"

@Serializable
data class PercentByPlaceOfPlayDto(
    @SerialName("home") val home: String,
    @SerialName("away") val away: String,
    @SerialName("all") val all: String
)

fun PercentByPlaceOfPlayDto.asDomainModel() = PercentByPlaceOfPlay(
    home = home,
    away = away,
    all = all
)

@Serializable
data class PointsDto(
    @SerialName("for") val forTeam: PointsStatisticsDto,
    @SerialName("against") val againstTeam: PointsStatisticsDto
)

fun PointsDto.asDomainModel() = Points(
    forTeam = forTeam.asDomainModel(),
    againstTeam = againstTeam.asDomainModel()
)

@Serializable
data class PointsStatisticsDto(
    @SerialName("total") val total: CountByPlaceOfPlayDto,
    @SerialName("average") val average: PercentByPlaceOfPlayDto
)

fun PointsStatisticsDto.asDomainModel() = PointsStatistics(
    total = total.asDomainModel(),
    average = average.asDomainModel()
)
