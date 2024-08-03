package ru.robilko.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.CountryEntity

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(entities: List<CountryEntity>)

    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<CountryEntity>>
}