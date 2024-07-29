package ru.robilko.remote.data.model

import com.google.gson.annotations.SerializedName
import ru.robilko.local.model.CountryEntity
import ru.robilko.model.data.Country

data class CountryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?,
    @SerializedName("flag") val flag: String?
)

fun CountryDto.asDomainModel() = Country(
    id = id,
    name = name,
    code = code.orEmpty(),
    flag = flag.orEmpty()
)

fun CountryDto.asEntity() = CountryEntity(
    id = id,
    name = name,
    code = code.orEmpty(),
    flag = flag.orEmpty()
)