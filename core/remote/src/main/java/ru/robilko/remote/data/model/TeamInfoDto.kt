package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.model.data.TeamInfo

data class TeamInfoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logoUrl: String,
    @SerializedName("nationnal") val isNational: Boolean,
    @SerializedName("country") val country: CountryDto
)

fun TeamInfoDto.asDomainModel(leagueId: Int) = TeamInfo(
    id = id,
    name = name,
    logoUrl = logoUrl,
    country = country.asDomainModel(),
    leagueId = leagueId
)
