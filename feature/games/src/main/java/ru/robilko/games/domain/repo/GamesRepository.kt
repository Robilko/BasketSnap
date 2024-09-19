package ru.robilko.games.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.GameResults

interface GamesRepository {
    suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String
    ): Response<List<GameResults>>
}