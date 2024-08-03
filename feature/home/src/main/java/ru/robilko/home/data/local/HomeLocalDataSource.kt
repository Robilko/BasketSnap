package ru.robilko.home.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.model.CountryEntity

interface HomeLocalDataSource {
    fun getCountries(): Flow<List<CountryEntity>>
    suspend fun insertCountries(entities: List<CountryEntity>)
}