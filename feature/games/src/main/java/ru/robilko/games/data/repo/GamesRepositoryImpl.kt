package ru.robilko.games.data.repo

import ru.robilko.base.util.Response
import ru.robilko.games.data.remote.GamesRemoteDataSource
import ru.robilko.games.domain.repo.GamesRepository
import ru.robilko.model.data.GameResults
import ru.robilko.model.data.Season
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val remoteDataSource: GamesRemoteDataSource
) : GamesRepository, SafeApiCall {
    override suspend fun getLeagueSeasons(leagueId: Int): Response<List<Season>> = safeApiCall {
        remoteDataSource.getLeagueSeasons(leagueId).map { it.asDomainModel() }
    }

    override suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String?,
        teamId: Int?
    ): Response<List<GameResults>> = safeApiCall {
        remoteDataSource.getGamesResults(
            leagueId = leagueId,
            season = season,
            date = date,
            teamId = teamId
        ).map { it.asDomainModel() }
    }
}