package ru.robilko.games.domain.useCases

import ru.robilko.games.domain.repo.GamesRepository
import javax.inject.Inject

class GetLeagueSeasonsUseCase @Inject constructor(
    private val repository: GamesRepository
) {
    suspend operator fun invoke(leagueId: Int) = repository.getLeagueSeasons(leagueId)
}