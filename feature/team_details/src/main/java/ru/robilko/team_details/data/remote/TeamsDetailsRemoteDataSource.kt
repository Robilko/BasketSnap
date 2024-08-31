package ru.robilko.team_details.data.remote

import ru.robilko.remote.data.model.SeasonDto
import ru.robilko.remote.data.model.TeamStatisticsResponse
import java.util.Date

interface TeamsDetailsRemoteDataSource {
    suspend fun getLeagueSeasons(leagueId: Int): List<SeasonDto>
    suspend fun getTeamStatistics(season: String, leagueId: Int, teamId: Int, date: Date? = null) : TeamStatisticsResponse?
}