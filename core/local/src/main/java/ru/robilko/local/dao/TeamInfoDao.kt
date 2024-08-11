package ru.robilko.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.TeamInfoEntity

@Dao
interface TeamInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamInfo(teamInfo: TeamInfoEntity)

    @Query("SELECT * FROM teams")
    fun getAllTeamInfos(): Flow<List<TeamInfoEntity>>

    @Query("DELETE FROM teams WHERE id = :id")
    suspend fun deleteTeamInfoById(id: Int)
}