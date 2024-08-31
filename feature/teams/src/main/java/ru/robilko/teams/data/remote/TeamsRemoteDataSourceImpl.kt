package ru.robilko.teams.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.TeamInfoDto
import javax.inject.Inject

class TeamsRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : TeamsRemoteDataSource {
    override suspend fun getTeamsInfo(leagueId: Int, season: String): List<TeamInfoDto> {
        return basketballApi.getTeamsInfo(leagueId = leagueId, season = season).response.orEmpty()
    }
}