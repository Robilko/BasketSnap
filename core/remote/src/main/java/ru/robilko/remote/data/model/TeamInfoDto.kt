package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.model.data.TeamInfo

@Serializable
data class TeamInfoDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("logo") val logoUrl: String,
    @SerialName("nationnal") val isNational: Boolean,
    @SerialName("country") val country: CountryDto
)

fun TeamInfoDto.asDomainModel(leagueId: Int) = TeamInfo(
    id = id,
    name = name,
    logoUrl = logoUrl,
    country = country.asDomainModel(),
    leagueId = leagueId
)
