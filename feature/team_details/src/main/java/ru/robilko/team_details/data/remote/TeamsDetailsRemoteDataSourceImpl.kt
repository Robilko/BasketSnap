package ru.robilko.team_details.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.SeasonDto
import ru.robilko.remote.data.model.TeamStatisticsResponse
import java.util.Date
import javax.inject.Inject

class TeamsDetailsRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : TeamsDetailsRemoteDataSource {
    override suspend fun getLeagueSeasons(leagueId: Int): List<SeasonDto> {
        return basketballApi.getLeagues(id = leagueId).response
            ?.firstOrNull()?.seasons
            ?.sortedByDescending { it.season }
            .orEmpty()
    }

    override suspend fun getTeamStatistics(
        season: String,
        leagueId: Int,
        teamId: Int,
        date: Date?
    ): TeamStatisticsResponse? {
        return basketballApi.getStatistics(
            season = season,
            leagueId = leagueId,
            teamId = teamId,
            date = date
        ).response
    }
}