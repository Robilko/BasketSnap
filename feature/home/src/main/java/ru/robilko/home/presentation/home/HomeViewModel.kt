package ru.robilko.home.presentation.home

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
import ru.robilko.home.R
import ru.robilko.home.domain.useCases.GetCountriesUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCountriesUseCase: GetCountriesUseCase,
) : BaseAppViewModel<HomeUiState, HomeUiEvent>() {
    private val _uiState = MutableStateFlow(HomeUiState(HomeDataState.Loading))
    override val uiState: StateFlow<HomeUiState> = _uiState

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ClickCountry -> {}
        }
    }

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            getCountriesUseCase().collect { response ->
                when (response) {
                    Response.Loading -> _uiState.update { it.copy(dataState = HomeDataState.Loading) }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                dataState = HomeDataState.Error(
                                    message = context.getString(
                                        R.string.getting_data_error
                                    ),
                                    onRetryAction = ::getCountries
                                )
                            )
                        }
                    }

                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                dataState = HomeDataState.Success(
                                    countries = response.data
                                        .sortedBy { country -> country.name }
                                        .toPersistentList()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}