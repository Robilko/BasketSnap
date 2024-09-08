package ru.robilko.league_details

import ru.robilko.remote.data.model.CountryDto
import ru.robilko.remote.data.model.LeagueDto

object LeagueDetailsMock {
    const val LEAGUE_ID = 1
    val leagueDto = LeagueDto(
        id = LEAGUE_ID,
        name = "NBA",
        type = "national",
        logoUrl = "",
        country = CountryDto(1, "USA", "US", ""),
        seasons = listOf()
    )
}