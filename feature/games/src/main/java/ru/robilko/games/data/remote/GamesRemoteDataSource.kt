package ru.robilko.games.data.remote

import ru.robilko.remote.data.model.GameResultsDto

interface GamesRemoteDataSource {
    suspend fun getGamesResults(leagueId: Int, season: String, date: String): List<GameResultsDto>
}