package ru.robilko.team_details.domain.useCases

import ru.robilko.team_details.domain.repo.TeamsDetailsRepository
import javax.inject.Inject

class GetLeagueSeasonsUseCase @Inject constructor(
    private val teamsDetailsRepository: TeamsDetailsRepository
) {
    suspend operator fun invoke(leagueId: Int) = teamsDetailsRepository.getLeagueSeasons(leagueId)
}