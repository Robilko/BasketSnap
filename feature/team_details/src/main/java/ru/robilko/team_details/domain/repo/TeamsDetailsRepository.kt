package ru.robilko.team_details.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.Season
import ru.robilko.model.data.TeamStatistics
import java.util.Date

interface TeamsDetailsRepository {
    suspend fun getLeagueSeasons(leagueId: Int): Response<List<Season>>
    suspend fun getTeamStatistics(
        season: String,
        leagueId: Int,
        teamId: Int,
        date: Date?
    ): Response<TeamStatistics?>
}