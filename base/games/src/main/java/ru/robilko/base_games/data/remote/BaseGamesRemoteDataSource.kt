package ru.robilko.base_games.data.remote

import ru.robilko.remote.data.model.GameResultsDto

interface BaseGamesRemoteDataSource {
    suspend fun getGamesResults(
        leagueId: Int?,
        season: String?,
        date: String? = null,
        teamId: Int? = null
    ): List<GameResultsDto>
}