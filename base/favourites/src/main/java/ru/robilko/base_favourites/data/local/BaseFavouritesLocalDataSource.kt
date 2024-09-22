package ru.robilko.base_favourites.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.LeagueWithSeasons
import ru.robilko.local.model.TeamInfoEntity

interface BaseFavouritesLocalDataSource {
    suspend fun insertLeagueWithSeasons(leagueWithSeasons: LeagueWithSeasons)
    fun getAllLeaguesWithSeasons(): Flow<List<LeagueWithSeasons>>
    suspend fun deleteLeagueById(id: Int)

    suspend fun insertTeamInfo(teamInfo: TeamInfoEntity)
    fun getAllTeamsInfo(): Flow<List<TeamInfoEntity>>
    suspend fun deleteTeamInfoById(teamId: Int, leagueId: Int)
}