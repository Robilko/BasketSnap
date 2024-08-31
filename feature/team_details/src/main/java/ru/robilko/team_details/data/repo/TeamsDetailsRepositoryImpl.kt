package ru.robilko.team_details.data.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.Season
import ru.robilko.model.data.TeamStatistics
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import ru.robilko.team_details.data.remote.TeamsDetailsRemoteDataSource
import ru.robilko.team_details.domain.repo.TeamsDetailsRepository
import java.util.Date
import javax.inject.Inject

class TeamsDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamsDetailsRemoteDataSource
) : TeamsDetailsRepository, SafeApiCall {
    override suspend fun getLeagueSeasons(leagueId: Int): Response<List<Season>> = safeApiCall {
        remoteDataSource.getLeagueSeasons(leagueId).map { it.asDomainModel() }
    }

    override suspend fun getTeamStatistics(
        season: String,
        leagueId: Int,
        teamId: Int,
        date: Date?
    ): Response<TeamStatistics?> = safeApiCall {
        remoteDataSource.getTeamStatistics(season, leagueId, teamId, date)?.asDomainModel()
    }
}