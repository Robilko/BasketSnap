package ru.robilko.base_seasons.data.remote

import ru.robilko.remote.data.model.SeasonDto

interface BaseSeasonsRemoteDataSource {
    suspend fun getSeasons(leagueId: Int): List<SeasonDto>
}