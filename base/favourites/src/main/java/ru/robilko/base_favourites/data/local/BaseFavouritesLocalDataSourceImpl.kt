package ru.robilko.base_favourites.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.dao.LeagueDao
import ru.robilko.local.model.LeagueWithSeasons
import javax.inject.Inject

data class BaseFavouritesLocalDataSourceImpl @Inject constructor(
    private val leagueDao: LeagueDao
) : BaseFavouritesLocalDataSource {
    override suspend fun insertLeagueWithSeasons(leagueWithSeasons: LeagueWithSeasons) {
        leagueDao.insertLeagueWithSeasons(leagueWithSeasons)
    }

    override fun getAllLeaguesWithSeasons(): Flow<List<LeagueWithSeasons>> {
        return leagueDao.getAllLeaguesWithSeasons()
    }

    override suspend fun deleteLeagueById(id: Int) {
        leagueDao.deleteLeagueById(id)
    }
}