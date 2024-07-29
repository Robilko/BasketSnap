package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName

data class DefaultResponseDto<T>(
    @SerializedName("results") val results: Int?,
    @SerializedName("response") val response: T?
)