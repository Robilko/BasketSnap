package ru.robilko.model.data

data class TeamInfo(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val country: Country,
    val leagueId: Int,
    val leagueName: String
)
