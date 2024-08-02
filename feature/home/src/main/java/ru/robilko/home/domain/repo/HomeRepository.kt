package ru.robilko.home.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.robilko.base.util.Response
import ru.robilko.model.data.Country
import ru.robilko.model.data.League

interface HomeRepository {
    suspend fun getCountries(): Flow<Response<List<Country>>>
}