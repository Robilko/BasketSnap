package ru.robilko.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.robilko.local.dao.CountryDao
import ru.robilko.local.dao.LeagueDao
import ru.robilko.local.model.CountryEntity
import ru.robilko.local.model.LeagueEntity
import ru.robilko.local.model.SeasonEntity


@Database(
    entities = [CountryEntity::class, LeagueEntity::class, SeasonEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class BasketSnapDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
}