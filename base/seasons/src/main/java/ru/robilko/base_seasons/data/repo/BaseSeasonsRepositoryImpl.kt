package ru.robilko.base_seasons.data.repo

import ru.robilko.base.util.Response
import ru.robilko.base_seasons.data.remote.BaseSeasonsRemoteDataSource
import ru.robilko.base_seasons.domain.repo.BaseSeasonsRepository
import ru.robilko.model.data.Season
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class BaseSeasonsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BaseSeasonsRemoteDataSource
) : BaseSeasonsRepository, SafeApiCall {
    override suspend fun getSeasons(leagueId: Int): Response<List<Season>> = safeApiCall {
        remoteDataSource.getSeasons(leagueId).map { it.asDomainModel() }
    }
}