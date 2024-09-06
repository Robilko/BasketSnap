package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import ru.robilko.model.data.Season

@Serializable
data class SeasonDto(
    @SerialName("season") val season: JsonElement,
    @SerialName("start") val start: String,
    @SerialName("end") val end: String
){
    fun getSeasonAsString(): String {
        return when {
            season.jsonPrimitive.isString -> season.jsonPrimitive.content
            season.jsonPrimitive.intOrNull != null -> season.jsonPrimitive.int.toString()
            else -> throw IllegalArgumentException("Invalid season format")
        }
    }
}

fun SeasonDto.asDomainModel() = Season(
    season = getSeasonAsString(),
    start = start,
    end = end
)
