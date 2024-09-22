package ru.robilko.base_seasons.domain.repo

import ru.robilko.base.util.Response
import ru.robilko.model.data.Season

interface BaseSeasonsRepository {
    suspend fun getSeasons(leagueId: Int): Response<List<Season>>
}