package ru.robilko.home.data.remote

import retrofit2.http.GET
import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.DefaultResponseDto

interface HomeApi {
    @GET("countries/")
    suspend fun getCountries(): DefaultResponseDto<List<CountryDto>>
}