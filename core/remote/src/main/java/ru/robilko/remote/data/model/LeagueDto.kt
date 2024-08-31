package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.model.data.League

@Serializable
data class LeagueDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("logo") val logoUrl: String,
    @SerialName("country") val country: CountryDto,
    @SerialName("seasons") val seasons: List<SeasonDto>
)

fun LeagueDto.asDomainModel() = League(
    id = id,
    name = name,
    type = type,
    logoUrl = logoUrl,
    country = country.asDomainModel(),
    seasons = seasons.map { it.asDomainModel() }.sortedByDescending { it.season }
)