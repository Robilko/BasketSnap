package ru.robilko.games.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.GameResults
import ru.robilko.model.data.Season

interface GamesRepository {
    suspend fun getLeagueSeasons(leagueId: Int): Response<List<Season>>

    suspend fun getGamesResults(
        leagueId: Int,
        season: String,
        date: String?,
        teamId: Int?
    ): Response<List<GameResults>>
}