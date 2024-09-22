package ru.robilko.base_games.data.repo

import ru.robilko.base.util.Response
import ru.robilko.base_games.data.remote.BaseGamesRemoteDataSource
import ru.robilko.base_games.domain.repo.BaseGamesRepository
import ru.robilko.model.data.GameResults
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class BaseGamesRepositoryImpl @Inject constructor(
    private val remoteDataSource: BaseGamesRemoteDataSource
) : BaseGamesRepository, SafeApiCall {
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