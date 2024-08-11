package ru.robilko.base_favourites.domain.useCases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.robilko.base.domain.FlowUseCase
import ru.robilko.base.util.Response
import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import ru.robilko.model.data.TeamInfo
import javax.inject.Inject

class GetFavouriteTeamsUseCase @Inject constructor(
    private val repo: BaseFavouritesRepository
) : FlowUseCase<Nothing?, List<TeamInfo>>() {
    override suspend fun execute(parameters: Nothing?): Flow<Response<List<TeamInfo>>> {
        return repo.getFavouritesTeamInfos().map { Response.Success(it) }
    }
}