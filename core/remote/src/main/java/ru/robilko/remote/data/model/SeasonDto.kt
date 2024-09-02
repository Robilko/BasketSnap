package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.model.data.Season

@Serializable
data class SeasonDto(
    @SerialName("season") val season: String,
    @SerialName("start") val start: String,
    @SerialName("end") val end: String
)

fun SeasonDto.asDomainModel() = Season(
    season = season,
    start = start,
    end = end
)
