package ru.robilko.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.robilko.local.BasketSnapDatabase
import ru.robilko.local.dao.CountryDao
import ru.robilko.local.dao.LeagueDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {
    @Provides
    @Singleton
    fun providesBasketSnapDatabase(
        @ApplicationContext context: Context
    ): BasketSnapDatabase = Room.databaseBuilder(
        context,
        BasketSnapDatabase::class.java,
        BASKET_SNAP_DB_NAME
    ).build()

    private const val BASKET_SNAP_DB_NAME = "basket_snap_database"

    @Provides
    fun providesCountryDao(
        database: BasketSnapDatabase
    ): CountryDao = database.countryDao()

    @Provides
    fun providesLeagueDao(
        database: BasketSnapDatabase
    ): LeagueDao = database.leagueDao()
}