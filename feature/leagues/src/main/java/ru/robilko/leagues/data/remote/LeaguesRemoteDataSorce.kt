package ru.robilko.leagues.data.remote

import ru.robilko.remote.data.model.LeagueDto

interface LeaguesRemoteDataSource {
    suspend fun getLeagues(countryId: Int): List<LeagueDto>
}