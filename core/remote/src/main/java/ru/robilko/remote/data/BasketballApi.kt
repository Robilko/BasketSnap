package ru.robilko.remote.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.DefaultResponseDto
import ru.robilko.remote.data.model.LeagueDto
import ru.robilko.remote.data.model.TeamInfoDto
import ru.robilko.remote.data.model.TeamStatisticsResponse
import java.util.Date

interface BasketballApi {
    @GET("countries/")
    suspend fun getCountries(): DefaultResponseDto<List<CountryDto>>

    @GET("leagues/")
    suspend fun getLeagues(
        @Query("country") country: String? = null,
        @Query("id") id: Int? = null,
        @Query("type") type: String? = null,
        @Query("season") season: String? = null,
        @Query("name") name: String? = null,
        @Query("code") code: String? = null,
        @Query("search") search: String? = null,
        @Query("country_id") countryId: Int? = null
    ): DefaultResponseDto<List<LeagueDto>>

    @GET("teams/")
    suspend fun getTeamsInfo(
        @Query("name") name: String? = null,
        @Query("id") id: Int? = null,
        @Query("league") leagueId: Int? = null,
        @Query("season") season: String? = null,
        @Query("search") search: String? = null
    ): DefaultResponseDto<List<TeamInfoDto>>

    @GET("statistics/")
    suspend fun getStatistics(
        @Query("season") season: String,
        @Query("league") leagueId: Int,
        @Query("team") teamId: Int,
        @Query("date") date: Date? = null
    ): DefaultResponseDto<TeamStatisticsResponse>
}