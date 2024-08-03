package ru.robilko.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.LeagueEntity
import ru.robilko.local.model.LeagueWithSeasons
import ru.robilko.local.model.SeasonEntity

@Dao
interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: LeagueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeasons(seasons: List<SeasonEntity>)

    @Transaction
    suspend fun insertLeagueWithSeasons(leagueWithSeasons: LeagueWithSeasons) {
        insertLeague(leagueWithSeasons.league)
        insertSeasons(leagueWithSeasons.seasons)
    }

    @Transaction
    @Query("SELECT * FROM leagues")
    fun getAllLeaguesWithSeasons(): Flow<List<LeagueWithSeasons>>

    @Query("DELETE FROM leagues WHERE id = :id")
    suspend fun deleteLeagueById(id: Int)
}