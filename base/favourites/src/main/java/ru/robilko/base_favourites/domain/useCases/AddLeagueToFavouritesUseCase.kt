package ru.robilko.base_favourites.domain.useCases

import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import ru.robilko.model.data.League
import javax.inject.Inject

class AddLeagueToFavouritesUseCase @Inject constructor(
    private val repo: BaseFavouritesRepository
) {
    suspend operator fun invoke(league: League) {
        repo.addLeagueToFavourites(league)
    }
}