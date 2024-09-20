package ru.robilko.games.data.remote

import ru.robilko.remote.data.model.GameResultsDto
import ru.robilko.remote.data.model.SeasonDto

interface GamesRemoteDataSource {
    suspend fun getLeagueSeasons(leagueId: Int): List<SeasonDto>
    suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String? = null,
        teamId: Int? = null
    ): List<GameResultsDto>
}