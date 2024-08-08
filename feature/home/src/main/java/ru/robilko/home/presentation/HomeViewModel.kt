package ru.robilko.home.presentation

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.home.domain.useCases.GetCountriesUseCase
import ru.robilko.model.data.Country
import javax.inject.Inject
import ru.robilko.core_ui.R as R_core_ui

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCountriesUseCase: GetCountriesUseCase,
) : BaseAppViewModel<HomeUiState, HomeUiEvent>() {
    private val _uiState = MutableStateFlow(HomeUiState(DataState.Loading))
    override val uiState: StateFlow<HomeUiState> = _uiState
    private var originalCountries: List<Country> = emptyList()

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.RefreshCountries -> getCountries()
            is HomeUiEvent.OnTextChange -> {
                _uiState.update { it.copy(searchQuery = event.searchQuery) }
                renderCountries()
            }
        }
    }

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            getCountriesUseCase().collect { response ->
                when (response) {
                    Response.Loading -> _uiState.update { it.copy(dataState = DataState.Loading) }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                dataState = DataState.Error(
                                    message = context.getString(
                                        R_core_ui.string.getting_data_error
                                    ),
                                    onRetryAction = ::getCountries
                                )
                            )
                        }
                    }

                    is Response.Success -> {
                        originalCountries = response.data
                        renderCountries()
                    }
                }
            }
        }
    }

    private fun renderCountries() {
        val searchQuery = _uiState.value.searchQuery
        val countries = originalCountries
            .filter { it.code.isNotEmpty() }
            .filterBySearchQuery(searchQuery)
            .sortedBy { it.name }
            .toPersistentList()
        val continents = originalCountries
            .filter { it.code.isBlank() }
            .filterBySearchQuery(searchQuery)
            .sortedBy { it.name }
            .toPersistentList()

        _uiState.update { state ->
            state.copy(
                countries = countries,
                continents = continents,
                dataState = DataState.Success
            )
        }
    }

    private fun List<Country>.filterBySearchQuery(text: String): List<Country> {
        return if (text.length < SEARCH_QUERY_MIN_CHARS_COUNT) this else filter {
            it.name.lowercase().contains(text.lowercase().trim())
        }
    }

    private companion object {
        const val SEARCH_QUERY_MIN_CHARS_COUNT = 3
    }
}