package ru.robilko.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.robilko.model.data.Country

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val code: String,
    @ColumnInfo(name = "flag_url") val flagUrl: String
)

fun CountryEntity.asDomainModel() = Country(
    id = id,
    name = name,
    code = code,
    flagUrl = flagUrl
)

fun Country.toEntity() = CountryEntity(
    id = id,
    name = name,
    code = code,
    flagUrl = flagUrl
)
