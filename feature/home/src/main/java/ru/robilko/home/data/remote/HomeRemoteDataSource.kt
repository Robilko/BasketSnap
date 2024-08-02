package ru.robilko.home.data.remote

import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.LeagueDto

interface HomeRemoteDataSource {
    suspend fun getCountries(): List<CountryDto>
}