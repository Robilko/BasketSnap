package ru.robilko.base_favourites.domain.useCases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.robilko.base.domain.FlowUseCase
import ru.robilko.base.util.Response
import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import ru.robilko.model.data.League
import javax.inject.Inject

class GetFavouriteLeaguesUseCase @Inject constructor(
    private val repository: BaseFavouritesRepository
) : FlowUseCase<Nothing?, List<League>>() {
    override suspend fun execute(parameters: Nothing?): Flow<Response<List<League>>> {
        return repository.getFavouritesLeagues().map { Response.Success(it) }
    }
}