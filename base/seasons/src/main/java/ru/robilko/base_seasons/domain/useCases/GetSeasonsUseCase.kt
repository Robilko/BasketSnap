package ru.robilko.base_seasons.domain.useCases

import ru.robilko.base_seasons.domain.repo.BaseSeasonsRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(
    private val repository: BaseSeasonsRepository
) {
    suspend operator fun invoke(leagueId: Int) = repository.getSeasons(leagueId)
}