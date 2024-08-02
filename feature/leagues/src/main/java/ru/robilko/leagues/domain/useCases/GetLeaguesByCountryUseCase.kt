package ru.robilko.leagues.domain.useCases

import ru.robilko.base.util.Response
import ru.robilko.leagues.domain.repo.LeaguesRepository
import ru.robilko.model.data.League
import javax.inject.Inject

class GetLeaguesByCountryUseCase @Inject constructor(
    private val repo: LeaguesRepository
) {
    suspend operator fun invoke(countryId: Int): Response<List<League>> {
        return repo.getLeagues(countryId)
    }
}