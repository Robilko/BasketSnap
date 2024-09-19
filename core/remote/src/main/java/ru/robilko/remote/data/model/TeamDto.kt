package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.model.data.GameTeamInfo

@Serializable
data class TeamDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("logo") val logoUrl: String
)

fun TeamDto.asDomainModel() = GameTeamInfo(
    id = id,
    name = name,
    logoUrl = logoUrl
)
