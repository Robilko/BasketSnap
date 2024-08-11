package ru.robilko.base_favourites.domain.useCases

import ru.robilko.base_favourites.domain.repo.BaseFavouritesRepository
import ru.robilko.model.data.TeamInfo
import javax.inject.Inject

class AddTeamToFavouritesUseCase @Inject constructor(
    private val repo: BaseFavouritesRepository
) {
    suspend operator fun invoke(teamInfo: TeamInfo) {
        repo.addTeamInfoToFavourites(teamInfo)
    }
}