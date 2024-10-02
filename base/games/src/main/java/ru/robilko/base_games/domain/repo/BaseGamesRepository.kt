package ru.robilko.base_games.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.GameResults

interface BaseGamesRepository {
    suspend fun getGamesResults(
        leagueId: Int?,
        season: String?,
        date: String?,
        teamId: Int?
    ): Response<List<GameResults>>
}