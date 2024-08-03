package ru.robilko.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.robilko.model.data.Country
import ru.robilko.model.data.League

@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    @ColumnInfo(name = "logo_url") val logoUrl: String,
    @ColumnInfo(name = "country_id") val countryId: Int,
    @ColumnInfo(name = "country_name") val countryName: String,
)

data class LeagueWithSeasons(
    @Embedded val league: LeagueEntity,
    @Relation(parentColumn = "id", entityColumn = "league_id") val seasons: List<SeasonEntity>
)

fun LeagueWithSeasons.asDomainModel() = League(
    id = league.id,
    name = league.name,
    type = league.type,
    logoUrl = league.logoUrl,
    country = Country(id = league.countryId, name = league.countryName, "", ""),
    seasons = seasons.map { it.asDomainModel() }
)

fun League.toEntity() = LeagueWithSeasons(
    league = LeagueEntity(
        id = id,
        name = name,
        type = type,
        logoUrl = logoUrl,
        countryId = country.id,
        countryName = country.name,
    ),
    seasons = seasons.map { it.toEntity(leagueId = id) }
)
