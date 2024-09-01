package ru.robilko.local.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.robilko.local.BasketSnapDatabase
import ru.robilko.local.dao.CountryDao
import ru.robilko.local.dao.LeagueDao
import ru.robilko.local.dao.TeamInfoDao
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {
    @Singleton
    @Provides
    @SharedPreferencesDefault
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(BASKET_SNAP_PREFS_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesBasketSnapDatabase(
        @ApplicationContext context: Context
    ): BasketSnapDatabase = Room.databaseBuilder(
        context,
        BasketSnapDatabase::class.java,
        BASKET_SNAP_DB_NAME
    ).build()

    @Provides
    fun providesCountryDao(
        database: BasketSnapDatabase
    ): CountryDao = database.countryDao()

    @Provides
    fun providesLeagueDao(
        database: BasketSnapDatabase
    ): LeagueDao = database.leagueDao()

    @Provides
    fun providesTeamDao(
        database: BasketSnapDatabase
    ): TeamInfoDao = database.teamInfoDao()

    private const val BASKET_SNAP_DB_NAME = "basket_snap_database"
    private const val BASKET_SNAP_PREFS_NAME = "basket_snap_preferences"
}

@Qualifier
annotation class SharedPreferencesDefault