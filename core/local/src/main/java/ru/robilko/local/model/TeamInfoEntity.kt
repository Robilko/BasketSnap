package ru.robilko.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.robilko.model.data.Country
import ru.robilko.model.data.TeamInfo

@Entity(tableName = "teams")
data class TeamInfoEntity(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo("logo_url") val logoUrl: String,
    @ColumnInfo("country_id") val countryId: Int,
    @ColumnInfo("country_name") val countryName: String,
    @ColumnInfo("league_id") val leagueId: Int
)

fun TeamInfo.toEntity() = TeamInfoEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    countryId = country.id,
    countryName = country.name,
    leagueId = leagueId
)

fun TeamInfoEntity.asDomainModel() = TeamInfo(
    id = id,
    name = name,
    logoUrl = logoUrl,
    country = Country(
        id = countryId,
        name = countryName,
        code = "",
        flagUrl = "",
    ),
    leagueId = leagueId
)
