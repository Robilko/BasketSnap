package ru.robilko.teams.domain.useCases

import ru.robilko.base.util.Response
import ru.robilko.model.data.TeamInfo
import ru.robilko.teams.domain.repo.TeamsRepository
import javax.inject.Inject

class GetTeamsInfoUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    suspend operator fun invoke(leagueId: Int, leagueName: String, season: String): Response<List<TeamInfo>> {
        return repo.getTeamsInfo(leagueId,leagueName, season)
    }
}