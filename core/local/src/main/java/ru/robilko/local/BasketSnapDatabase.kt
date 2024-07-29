package ru.robilko.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.robilko.local.dao.CountryDao
import ru.robilko.local.model.CountryEntity


@Database(
    entities = [CountryEntity::class, ],
    version = 1
)
internal abstract class BasketSnapDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}