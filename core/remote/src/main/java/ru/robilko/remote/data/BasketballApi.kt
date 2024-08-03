package ru.robilko.remote.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.DefaultResponseDto
import ru.robilko.remote.data.model.LeagueDto

interface BasketballApi {
    @GET("countries/")
    suspend fun getCountries(): DefaultResponseDto<List<CountryDto>>

    @GET("leagues/")
    suspend fun getLeagues(
        @Query("country_id") countryId: Int
    ):DefaultResponseDto<List<LeagueDto>>
}