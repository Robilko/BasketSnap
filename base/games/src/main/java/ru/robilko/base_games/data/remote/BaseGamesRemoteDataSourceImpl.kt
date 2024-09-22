package ru.robilko.base_games.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.GameResultsDto
import javax.inject.Inject

class BaseGamesRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : BaseGamesRemoteDataSource {
    override suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String?,
        teamId: Int?
    ): List<GameResultsDto> {
        return basketballApi.getGames(
            season = season,
            leagueId = leagueId,
            date = date,
            teamId = teamId
        ).response.orEmpty()
    }
}