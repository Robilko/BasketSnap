package ru.robilko.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.robilko.model.data.Country
import ru.robilko.model.data.TeamInfo

@Entity(tableName = "teams")
data class TeamInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("team_id") val teamId: Int,
    @ColumnInfo("team_name") val teamName: String,
    @ColumnInfo("logo_url") val logoUrl: String,
    @ColumnInfo("country_id") val countryId: Int,
    @ColumnInfo("country_name") val countryName: String,
    @ColumnInfo("league_id") val leagueId: Int,
    @ColumnInfo("league_name") val leagueName: String
)

fun TeamInfo.toEntity() = TeamInfoEntity(
    teamId = id,
    teamName = name,
    logoUrl = logoUrl,
    countryId = country.id,
    countryName = country.name,
    leagueId = leagueId,
    leagueName = leagueName
)

fun TeamInfoEntity.asDomainModel() = TeamInfo(
    id = teamId,
    name = teamName,
    logoUrl = logoUrl,
    country = Country(
        id = countryId,
        name = countryName,
        code = "",
        flagUrl = "",
    ),
    leagueId = leagueId,
    leagueName = leagueName
)
