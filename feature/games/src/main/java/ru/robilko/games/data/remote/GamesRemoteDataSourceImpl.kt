package ru.robilko.games.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.GameResultsDto
import javax.inject.Inject

class GamesRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : GamesRemoteDataSource {
    override suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String
    ): List<GameResultsDto> {
        return basketballApi.getGames(
            season = season,
            leagueId = leagueId,
            date = null
        ).response.orEmpty()
    }
}