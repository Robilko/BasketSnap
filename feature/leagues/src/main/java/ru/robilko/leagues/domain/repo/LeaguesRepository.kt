package ru.robilko.leagues.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.League

interface LeaguesRepository {
    suspend fun getLeagues(countryId: Int): Response<List<League>>
}