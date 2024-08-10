package ru.robilko.league_details.domain.useCases

import ru.robilko.base.util.Response
import ru.robilko.league_details.domain.repo.LeagueDetailsRepository
import ru.robilko.model.data.League
import javax.inject.Inject

class GetLeagueUseCase @Inject constructor(
    private val repo: LeagueDetailsRepository
) {
    suspend operator fun invoke(id: Int): Response<League?> {
        return repo.getLeague(id)
    }
}