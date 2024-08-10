package ru.robilko.leagues.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.LeagueDto
import javax.inject.Inject

class LeaguesRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : LeaguesRemoteDataSource {

    override suspend fun getLeagues(countryId: Int): List<LeagueDto> {
        return basketballApi.getLeagues(countryId = countryId).response.orEmpty()
    }
}