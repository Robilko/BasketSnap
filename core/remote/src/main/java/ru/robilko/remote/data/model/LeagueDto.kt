package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.model.data.League

data class LeagueDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("logo") val logoUrl: String,
    @SerializedName("country") val country: CountryDto,
    @SerializedName("seasons") val seasons: List<SeasonDto>
)

fun LeagueDto.asDomainModel() = League(
    id = id,
    name = name,
    type = type,
    logoUrl = logoUrl,
    country = country.asDomainModel(),
    seasons = seasons.map { it.asDomainModel() }.sortedByDescending { it.season }
)