package ru.robilko.home.data.local

import kotlinx.coroutines.flow.Flow
import ru.robilko.local.dao.CountryDao
import ru.robilko.local.model.CountryEntity
import javax.inject.Inject

class HomeLocalDataSourceImpl @Inject constructor(
    private val dao: CountryDao
) : HomeLocalDataSource {
    override fun getCountries(): Flow<List<CountryEntity>> {
        return dao.getAllCountries()
    }

    override suspend fun insertCountries(entities: List<CountryEntity>) {
        dao.insertCountries(entities)
    }
}