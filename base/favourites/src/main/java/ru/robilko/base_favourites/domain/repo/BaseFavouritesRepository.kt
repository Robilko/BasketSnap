package ru.robilko.base_favourites.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.robilko.model.data.League

interface BaseFavouritesRepository {
    suspend fun addLeagueToFavourites(league: League)
    fun getFavouritesLeagues(): Flow<List<League>>
    suspend fun deleteLeagueFromFavourites(id: Int)
}