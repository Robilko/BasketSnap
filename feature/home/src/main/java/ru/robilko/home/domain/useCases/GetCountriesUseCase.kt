package ru.robilko.home.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.robilko.base.domain.FlowUseCase
import ru.robilko.base.util.Response
import ru.robilko.home.domain.repo.HomeRepository
import ru.robilko.model.data.Country
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) : FlowUseCase<Nothing?, List<Country>>() {
    override suspend fun execute(parameters: Nothing?): Flow<Response<List<Country>>> {
        return homeRepository.getCountries()
    }
}