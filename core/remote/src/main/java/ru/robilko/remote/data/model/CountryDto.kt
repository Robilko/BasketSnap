package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.local.model.CountryEntity
import ru.robilko.model.data.Country

data class CountryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?,
    @SerializedName("flag") val flagUrl: String?
)

fun CountryDto.asDomainModel() = Country(
    id = id,
    name = name,
    code = code.orEmpty(),
    flagUrl = flagUrl.orEmpty()
)

fun CountryDto.asEntity() = CountryEntity(
    id = id,
    name = name,
    code = code?.trim().orEmpty(),
    flagUrl = flagUrl.orEmpty()
)