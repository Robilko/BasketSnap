package ru.robilko.games.domain.useCases

import ru.robilko.base.util.Response
import ru.robilko.games.domain.repo.GamesRepository
import ru.robilko.model.data.GameResults
import javax.inject.Inject

class GetGamesResultsUseCase @Inject constructor(
    private val repo: GamesRepository
) {
    suspend operator fun invoke(
        leagueId: Int,
        season: String,
        date: String
    ): Response<List<GameResults>> {
        return repo.getGamesResults(leagueId = leagueId, season = season, date = date)
    }
}