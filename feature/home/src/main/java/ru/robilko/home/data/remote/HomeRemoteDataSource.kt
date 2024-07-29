package ru.robilko.home.data.remote

import ru.robilko.remote.data.model.CountryDto

interface HomeRemoteDataSource {
    suspend fun getCountries(): List<CountryDto>
}