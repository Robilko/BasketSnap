package ru.robilko.games.data.repo

import ru.robilko.base.util.Response
import ru.robilko.games.data.remote.GamesRemoteDataSource
import ru.robilko.games.domain.repo.GamesRepository
import ru.robilko.model.data.GameResults
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val remoteDataSource: GamesRemoteDataSource
) : GamesRepository, SafeApiCall {
    override suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String
    ): Response<List<GameResults>> = safeApiCall {
        remoteDataSource.getGamesResults(
            leagueId = leagueId,
            season = season,
            date = date
        ).map { it.asDomainModel() }
    }
}