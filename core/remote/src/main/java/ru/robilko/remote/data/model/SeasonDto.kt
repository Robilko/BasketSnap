package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.robilko.model.data.Season
import ru.robilko.remote.util.asString

@Serializable
data class SeasonDto(
    @SerialName("season") val season: JsonElement,
    @SerialName("start") val start: String,
    @SerialName("end") val end: String
)

fun SeasonDto.asDomainModel() = Season(
    season = season.asString().orEmpty(),
    start = start,
    end = end
)
