package ru.robilko.base_favourites.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.robilko.model.data.League
import ru.robilko.model.data.TeamInfo

interface BaseFavouritesRepository {
    suspend fun addLeagueToFavourites(league: League)
    fun getFavouritesLeagues(): Flow<List<League>>
    suspend fun deleteLeagueFromFavourites(id: Int)

    suspend fun addTeamInfoToFavourites(teamInfo: TeamInfo)
    fun getFavouritesTeamInfos(): Flow<List<TeamInfo>>
    suspend fun deleteTeamInfoFromFavourites(id: Int)
}