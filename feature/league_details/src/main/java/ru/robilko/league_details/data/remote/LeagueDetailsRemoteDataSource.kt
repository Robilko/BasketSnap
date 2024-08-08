package ru.robilko.league_details.data.remote

import ru.robilko.remote.data.model.LeagueDto

interface LeagueDetailsRemoteDataSource {
    suspend fun getLeague(id: Int): LeagueDto?
}