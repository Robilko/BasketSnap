package ru.robilko.home.data.remote

import android.util.Log
import ru.robilko.remote.data.model.CountryDto
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRemoteDataSource {
    override suspend fun getCountries(): List<CountryDto> {
        Log.d("TAG", "getCountries: romote ")
        return homeApi.getCountries().response.orEmpty()
    }
}