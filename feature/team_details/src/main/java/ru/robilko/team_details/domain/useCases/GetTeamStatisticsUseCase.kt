package ru.robilko.team_details.domain.useCases

import ru.robilko.team_details.domain.repo.TeamsDetailsRepository
import java.util.Date
import javax.inject.Inject

class GetTeamStatisticsUseCase @Inject constructor(
    private val teamsDetailsRepository: TeamsDetailsRepository
) {
    suspend operator fun invoke(
        season: String,
        leagueId: Int,
        teamId: Int,
        date: Date? = null
    ) = teamsDetailsRepository.getTeamStatistics(season, leagueId, teamId, date)
}