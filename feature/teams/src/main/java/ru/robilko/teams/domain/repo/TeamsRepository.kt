package ru.robilko.teams.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.TeamInfo

interface TeamsRepository {
    suspend fun getTeamsInfo(leagueId: Int, leagueName: String, season: String): Response<List<TeamInfo>>
}