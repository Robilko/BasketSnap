package ru.robilko.league_details.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.LeagueDto
import javax.inject.Inject

class LeagueDetailsRemoteDataSourceImpl @Inject constructor(
    private val api: BasketballApi
) : LeagueDetailsRemoteDataSource {
    override suspend fun getLeague(id: Int): LeagueDto? {
        return api.getLeagues(id = id).response?.firstOrNull()
    }
}