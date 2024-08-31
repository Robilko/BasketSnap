package ru.robilko.base_favourites.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.dao.LeagueDao
import ru.robilko.local.dao.TeamInfoDao
import ru.robilko.local.model.LeagueWithSeasons
import ru.robilko.local.model.TeamInfoEntity
import javax.inject.Inject

data class BaseFavouritesLocalDataSourceImpl @Inject constructor(
    private val leagueDao: LeagueDao,
    private val teamInfoDao: TeamInfoDao
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

    override suspend fun insertTeamInfo(teamInfo: TeamInfoEntity) {
        teamInfoDao.insertTeamInfo(teamInfo)
    }

    override fun getAllTeamsInfo(): Flow<List<TeamInfoEntity>> {
        return teamInfoDao.getAllTeamInfos()
    }

    override suspend fun deleteTeamInfoById(id: Int) {
        teamInfoDao.deleteTeamInfoById(id)
    }
}