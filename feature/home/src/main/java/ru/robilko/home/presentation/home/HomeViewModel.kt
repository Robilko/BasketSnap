package ru.robilko.home.presentation.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.home.domain.useCases.GetCountriesUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
) : BaseAppViewModel<HomeUiState, HomeUiEvent>() {
    private val _uiState = MutableStateFlow(HomeUiState())
    override val uiState: StateFlow<HomeUiState> = _uiState

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.ClickCountriesButton -> getCountries()
        }
    }

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            getCountriesUseCase().collect { response ->
                when (response) {
                    Response.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Response.Failure -> TODO()
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                countries = response.data.toPersistentList()
                            )
                        }
                    }
                }
            }
        }
    }
}