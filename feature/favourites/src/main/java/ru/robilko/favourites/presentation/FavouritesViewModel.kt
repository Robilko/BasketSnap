package ru.robilko.favourites.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.robilko.core_ui.presentation.BaseAppViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(

) : BaseAppViewModel<FavouritesUiState, FavouritesUiEvent>() {

    private val _uiState: MutableStateFlow<FavouritesUiState> =
        MutableStateFlow(FavouritesUiState())
    override val uiState: StateFlow<FavouritesUiState> = _uiState

    override fun onEvent(event: FavouritesUiEvent) {
        when (event) {
            else -> {}
        }
    }
}