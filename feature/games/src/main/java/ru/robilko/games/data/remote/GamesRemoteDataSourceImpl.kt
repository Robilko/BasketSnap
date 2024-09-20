package ru.robilko.games.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.GameResultsDto
import ru.robilko.remote.data.model.SeasonDto
import ru.robilko.remote.util.asString
import javax.inject.Inject

class GamesRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : GamesRemoteDataSource {
    override suspend fun getLeagueSeasons(leagueId: Int): List<SeasonDto> {
        return basketballApi.getLeagues(id = leagueId).response
            ?.firstOrNull()?.seasons
            ?.sortedByDescending { it.season.asString() }
            .orEmpty()
    }

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