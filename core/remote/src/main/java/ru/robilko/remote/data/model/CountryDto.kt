package ru.robilko.remote.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.robilko.local.model.CountryEntity
import ru.robilko.model.data.Country

@Serializable
data class CountryDto(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("code") val code: String?,
    @SerialName("flag") val flagUrl: String?
)

fun CountryDto.asDomainModel() = Country(
    id = id ?: 0,
    name = name.orEmpty(),
    code = code.orEmpty(),
    flagUrl = flagUrl.orEmpty()
)

fun CountryDto.asEntity() = CountryEntity(
    id = id ?: 0,
    name = name.orEmpty(),
    code = code?.trim().orEmpty(),
    flagUrl = flagUrl.orEmpty()
)