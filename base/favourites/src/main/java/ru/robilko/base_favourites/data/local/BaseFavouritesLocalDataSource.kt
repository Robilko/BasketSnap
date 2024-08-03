package ru.robilko.base_favourites.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.LeagueWithSeasons

interface BaseFavouritesLocalDataSource {
    suspend fun insertLeagueWithSeasons(leagueWithSeasons: LeagueWithSeasons)
    fun getAllLeaguesWithSeasons(): Flow<List<LeagueWithSeasons>>
    suspend fun deleteLeagueById(id: Int)
}