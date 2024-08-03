package ru.robilko.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.robilko.model.data.Season

@Entity(
    tableName = "seasons", foreignKeys = [
        ForeignKey(
            entity = LeagueEntity::class,
            parentColumns = ["id"],
            childColumns = ["league_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["league_id"])]
)
data class SeasonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "league_id") val leagueId: Int,
    val season: String,
    val start: String,
    val end: String
)

fun SeasonEntity.asDomainModel() = Season(
    season = season,
    start = start,
    end = end
)

fun Season.toEntity(leagueId: Int) = SeasonEntity(
    leagueId = leagueId,
    season = season,
    start = start,
    end = end
)
