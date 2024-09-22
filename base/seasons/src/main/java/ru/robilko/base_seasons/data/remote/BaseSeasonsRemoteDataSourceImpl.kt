package ru.robilko.base_seasons.data.remote

import ru.robilko.remote.data.BasketballApi
import ru.robilko.remote.data.model.SeasonDto
import ru.robilko.remote.util.asString
import javax.inject.Inject

class BaseSeasonsRemoteDataSourceImpl @Inject constructor(
    private val basketballApi: BasketballApi
) : BaseSeasonsRemoteDataSource {
    override suspend fun getSeasons(leagueId: Int): List<SeasonDto> {
        return basketballApi.getLeagues(id = leagueId).response
            ?.firstOrNull()?.seasons
            ?.sortedByDescending { it.season.asString() }
            .orEmpty()
    }
}