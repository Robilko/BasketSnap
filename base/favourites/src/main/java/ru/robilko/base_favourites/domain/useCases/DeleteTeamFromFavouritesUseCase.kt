package ru.robilko.base_favourites.domain.useCases

import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import javax.inject.Inject

class DeleteTeamFromFavouritesUseCase @Inject constructor(
    private val repo: BaseFavouritesRepository
) {
    suspend operator fun invoke(teamId: Int, leagueId: Int) {
        repo.deleteTeamInfoFromFavourites(teamId, leagueId)
    }
}