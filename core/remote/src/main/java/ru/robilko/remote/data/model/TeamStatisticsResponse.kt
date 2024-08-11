package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.model.data.CountByPlaceOfPlay
import ru.robilko.model.data.GamesInfo
import ru.robilko.model.data.GamesResults
import ru.robilko.model.data.LeagueShortInfo
import ru.robilko.model.data.PercentByPlaceOfPlay
import ru.robilko.model.data.Points
import ru.robilko.model.data.PointsStatistics
import ru.robilko.model.data.TeamStatistics
import ru.robilko.model.data.TotalResult

data class TeamStatisticsResponse(
    @SerializedName("country") val country: CountryDto,
    @SerializedName("league") val league: LeagueShortInfoDto,
    @SerializedName("team") val team: TeamDto,
    @SerializedName("games") val games: GamesInfoDto,
    @SerializedName("points") val points: PointsDto
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

data class LeagueShortInfoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("logo") val logoUrl: String
)

fun LeagueShortInfoDto.asDomainModel() = LeagueShortInfo(
    id = id,
    name = name,
    type = type,
    logoUrl = logoUrl
)

data class GamesInfoDto(
    @SerializedName("played") val played: CountByPlaceOfPlayDto,
    @SerializedName("wins") val wins: GamesResultsDto,
    @SerializedName("draws") val draws: GamesResultsDto,
    @SerializedName("loses") val loses: GamesResultsDto
)

fun GamesInfoDto.asDomainModel() = GamesInfo(
    played = played.asDomainModel(),
    wins = wins.asDomainModel(),
    draws = draws.asDomainModel(),
    loses = loses.asDomainModel()
)

data class CountByPlaceOfPlayDto(
    @SerializedName("home") val home: Int,
    @SerializedName("away") val away: Int,
    @SerializedName("all") val all: Int
)

fun CountByPlaceOfPlayDto.asDomainModel() = CountByPlaceOfPlay(
    home = home,
    away = away,
    all = all
)

data class GamesResultsDto(
    @SerializedName("home") val home: TotalResultDto,
    @SerializedName("away") val away: TotalResultDto,
    @SerializedName("all") val all: TotalResultDto
)

fun GamesResultsDto.asDomainModel() = GamesResults(
    home = home.asDomainModel(),
    away = away.asDomainModel(),
    all = all.asDomainModel()
)

data class TotalResultDto(
    @SerializedName("total") val total: Int,
    @SerializedName("percentage") val percentage: String
)

fun TotalResultDto.asDomainModel() = TotalResult(
    total = total,
    percentage = percentage
)

data class PercentByPlaceOfPlayDto(
    @SerializedName("home") val home: String,
    @SerializedName("away") val away: String,
    @SerializedName("all") val all: String
)

fun PercentByPlaceOfPlayDto.asDomainModel() = PercentByPlaceOfPlay(
    home = home,
    away = away,
    all = all
)

data class PointsDto(
    @SerializedName("for") val forTeam: PointsStatisticsDto,
    @SerializedName("against") val againstTeam: PointsStatisticsDto
)

fun PointsDto.asDomainModel() = Points(
    forTeam = forTeam.asDomainModel(),
    againstTeam = againstTeam.asDomainModel()
)

data class PointsStatisticsDto(
    @SerializedName("total") val total: CountByPlaceOfPlayDto,
    @SerializedName("average") val average: PercentByPlaceOfPlayDto
)

fun PointsStatisticsDto.asDomainModel() = PointsStatistics(
    total = total.asDomainModel(),
    average = average.asDomainModel()
)
