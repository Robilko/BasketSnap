package ru.robilko.league_details.data.repo

import ru.robilko.base.util.Response
import ru.robilko.league_details.data.remote.LeagueDetailsRemoteDataSource
import ru.robilko.league_details.domain.repo.LeagueDetailsRepository
import ru.robilko.model.data.League
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class LeagueDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: LeagueDetailsRemoteDataSource
) : LeagueDetailsRepository, SafeApiCall {
    override suspend fun getLeague(id: Int): Response<League?> = safeApiCall {
        remoteDataSource.getLeague(id)?.asDomainModel()
    }
}