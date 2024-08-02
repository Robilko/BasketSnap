package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.model.data.Season

data class SeasonDto(
    @SerializedName("season") val season: String,
    @SerializedName("start") val start: String,
    @SerializedName("end") val end: String
)

fun SeasonDto.asDomainModel() = Season(
    season = season,
    start = start,
    end = end
)
