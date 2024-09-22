package ru.robilko.teams.data.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.TeamInfo
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import ru.robilko.teams.data.remote.TeamsRemoteDataSource
import ru.robilko.teams.domain.repo.TeamsRepository
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamsRemoteDataSource,
) : TeamsRepository, SafeApiCall {
    override suspend fun getTeamsInfo(
        leagueId: Int,
        leagueName: String,
        season: String
    ): Response<List<TeamInfo>> =
        safeApiCall {
            remoteDataSource.getTeamsInfo(leagueId = leagueId, season = season)
                .map { it.asDomainModel(leagueId = leagueId, leagueName = leagueName) }
        }
}