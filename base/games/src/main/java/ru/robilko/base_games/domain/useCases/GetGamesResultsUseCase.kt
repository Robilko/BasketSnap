package ru.robilko.base_games.domain.useCases

import ru.robilko.base.util.Response
import ru.robilko.base_games.domain.repo.BaseGamesRepository
import ru.robilko.model.data.GameResults
import javax.inject.Inject

class GetGamesResultsUseCase @Inject constructor(
    private val repo: BaseGamesRepository
) {
    suspend operator fun invoke(
        leagueId: Int,
        season: String,
        date: String? = null,
        teamId: Int? = null
    ): Response<List<GameResults>> {
        return repo.getGamesResults(
            leagueId = leagueId,
            season = season,
            date = date,
            teamId = teamId
        )
    }
}