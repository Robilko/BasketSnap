package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logoUrl: String
)
