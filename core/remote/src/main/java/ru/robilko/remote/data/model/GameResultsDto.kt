package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.robilko.base.util.ISO_8601_WITH_TIMEZONE_PATTERN
import ru.robilko.base.util.toDate
import ru.robilko.model.data.GameLeague
import ru.robilko.model.data.GameResults
import ru.robilko.model.data.GameScore
import ru.robilko.remote.util.asString

@Serializable
data class GameResultsDto(
    @SerialName("id") val id: Int,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("timezone") val timezone: String,
    @SerialName("venue") val venue: String?,
    @SerialName("status") val status: GameStatusDto,
    @SerialName("league") val league: GameLeagueDto,
    @SerialName("country") val country: CountryDto,
    @SerialName("teams") val teams: GameTeamsDto,
    @SerialName("scores") val scores: GameScoresDto
)

fun GameResultsDto.asDomainModel() = GameResults(
    id = id,
    date = date.toDate(ISO_8601_WITH_TIMEZONE_PATTERN, timeZoneId = timezone),
    time = time,
    timestamp = timestamp,
    timezone = timezone,
    venue = venue.orEmpty(),
    statusLong = status.long,
    statusShort = status.short,
    timer = status.timer,
    league = league.asDomainModel(),
    country = country.asDomainModel(),
    homeTeam = teams.homeTeam.asDomainModel(),
    awayTeam = teams.awayTeam.asDomainModel(),
    homeScore = scores.homeScore.asDomainModel(),
    awayScore = scores.awayScore.asDomainModel()
)

@Serializable
data class GameStatusDto(
    @SerialName("long") val long: String,
    @SerialName("short") val short: String,
    @SerialName("timer") val timer: String?
)

@Serializable
data class GameLeagueDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("season") val season: JsonElement,
    @SerialName("logo") val logoUrl: String,
)

fun GameLeagueDto.asDomainModel() = GameLeague(
    id = id,
    name = name,
    type = type,
    season = season.asString().orEmpty(),
    logoUrl = logoUrl
)

@Serializable
data class GameTeamsDto(
    @SerialName("home") val homeTeam: TeamDto,
    @SerialName("away") val awayTeam: TeamDto
)

@Serializable
data class ScoreDto(
    @SerialName("quarter_1") val quarter1: Int?,
    @SerialName("quarter_2") val quarter2: Int?,
    @SerialName("quarter_3") val quarter3: Int?,
    @SerialName("quarter_4") val quarter4: Int?,
    @SerialName("over_time") val overTime: Int?,
    @SerialName("total") val total: Int?
)

fun ScoreDto.asDomainModel() = GameScore(
    quarter1 = quarter1,
    quarter2 = quarter2,
    quarter3 = quarter3,
    quarter4 = quarter4,
    overTime = overTime,
    total = total
)

@Serializable
data class GameScoresDto(
    @SerialName("home") val homeScore: ScoreDto,
    @SerialName("away") val awayScore: ScoreDto
)