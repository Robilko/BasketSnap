package ru.robilko.league_details.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.League

interface LeagueDetailsRepository {
    suspend fun getLeague(id: Int): Response<League?>
}