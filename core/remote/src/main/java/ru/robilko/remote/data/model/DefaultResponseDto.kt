package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponseDto<T>(
    @SerialName("results") val results: Int?,
    @SerialName("response") val response: T?
)