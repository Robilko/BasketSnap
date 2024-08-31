package ru.robilko.teams.data.remote

import ru.robilko.remote.data.model.TeamInfoDto

interface TeamsRemoteDataSource {
    suspend fun getTeamsInfo(leagueId: Int, season: String): List<TeamInfoDto>
}