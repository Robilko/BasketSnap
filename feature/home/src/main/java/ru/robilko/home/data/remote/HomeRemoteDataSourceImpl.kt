package ru.robilko.home.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.LeagueDto
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : HomeRemoteDataSource {
    override suspend fun getCountries(): List<CountryDto> {
        return basketballApi.getCountries().response.orEmpty()
    }
}