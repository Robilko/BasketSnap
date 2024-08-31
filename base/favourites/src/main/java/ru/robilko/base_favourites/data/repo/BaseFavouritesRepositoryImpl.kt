package ru.robilko.base_favourites.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.robilko.base.util.DispatchersProvider
import ru.robilko.base_favourites.data.local.BaseFavouritesLocalDataSource
import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import ru.robilko.local.model.asDomainModel
import ru.robilko.local.model.toEntity
import ru.robilko.model.data.League
import ru.robilko.model.data.TeamInfo
import javax.inject.Inject

class BaseFavouritesRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val localDataSource: BaseFavouritesLocalDataSource
) : BaseFavouritesRepository {
    override suspend fun addLeagueToFavourites(league: League) {
        localDataSource.insertLeagueWithSeasons(league.toEntity())
    }

    override fun getFavouritesLeagues(): Flow<List<League>> {
        return localDataSource.getAllLeaguesWithSeasons()
            .map { it.map { entity -> entity.asDomainModel() } }
            .distinctUntilChanged()
            .flowOn(dispatchersProvider.io)
    }

    override suspend fun deleteLeagueFromFavourites(id: Int) {
        localDataSource.deleteLeagueById(id)
    }

    override suspend fun addTeamInfoToFavourites(teamInfo: TeamInfo) {
        localDataSource.insertTeamInfo(teamInfo.toEntity())
    }

    override fun getFavouritesTeamInfos(): Flow<List<TeamInfo>> {
        return localDataSource.getAllTeamsInfo().map { it.map { entity -> entity.asDomainModel() } }
    }

    override suspend fun deleteTeamInfoFromFavourites(id: Int) {
        localDataSource.deleteTeamInfoById(id)
    }
}