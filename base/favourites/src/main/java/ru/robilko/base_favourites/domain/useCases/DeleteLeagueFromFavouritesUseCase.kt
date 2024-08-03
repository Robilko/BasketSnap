package ru.robilko.base_favourites.domain.useCases

import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import javax.inject.Inject

class DeleteLeagueFromFavouritesUseCase @Inject constructor(
    private val repository: BaseFavouritesRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteLeagueFromFavourites(id)
    }
}