package ru.robilko.home.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.robilko.base.util.DispatchersProvider
import ru.robilko.base.util.Response
import ru.robilko.base.util.onSuccess
import ru.robilko.home.data.local.HomeLocalDataSource
import ru.robilko.home.data.remote.HomeRemoteDataSource
import ru.robilko.home.domain.repo.HomeRepository
import ru.robilko.local.model.asDomainModel
import ru.robilko.model.data.Country
import ru.robilko.remote.data.model.asEntity
import ru.robilko.remote.util.SafeApiCall
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource
) : HomeRepository, SafeApiCall {
    override suspend fun getCountries(): Flow<Response<List<Country>>> {
        return localDataSource.getCountries().map { entities ->
            if (entities.isEmpty()) {
                val remoteResult = safeApiCall { remoteDataSource.getCountries() }
                remoteResult.onSuccess { result ->
                    if (result.data.isNotEmpty()) {
                        localDataSource.insertCountries(result.data.map { it.asEntity() })
                    }
                }
            }
            Response.Success(entities.map { it.asDomainModel() })
        }.distinctUntilChanged()
            .flowOn(dispatchers.io)
    }
}