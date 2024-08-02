package ru.robilko.leagues.data.repo

import ru.robilko.base.util.Response
import ru.robilko.leagues.data.remote.LeaguesRemoteDataSource
import ru.robilko.leagues.domain.repo.LeaguesRepository
import ru.robilko.model.data.League
import ru.robilko.remote.data.model.asDomainModel
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class LeaguesRepositoryImpl @Inject constructor(
    private val remoteDataSource: LeaguesRemoteDataSource
) : LeaguesRepository, SafeApiCall {
    override suspend fun getLeagues(countryId: Int): Response<List<League>> =
        safeApiCall { remoteDataSource.getLeagues(countryId).map { it.asDomainModel() } }
}