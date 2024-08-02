package ru.robilko.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.robilko.model.data.Country

@Entity(tableName = "country_entity")
data class CountryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val code: String,
    val flagUrl: String
)

fun CountryEntity.asDomainModel() = Country(
    id = id,
    name = name,
    code = code,
    flagUrl = flagUrl
)
