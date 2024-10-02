package ru.robilko.home.presentation

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.robilko.base.util.Response
import ru.robilko.base.util.SERVER_DATE_PATTERN
import ru.robilko.base.util.onFailure
import ru.robilko.base.util.onSuccess
import ru.robilko.base.util.toStringDate
import ru.robilko.base_favourites.domain.useCases.GetFavouriteLeaguesUseCase
import ru.robilko.base_favourites.domain.useCases.GetFavouriteTeamsUseCase
import ru.robilko.base_games.domain.useCases.GetGamesResultsUseCase
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.core_ui.R
import ru.robilko.core_ui.presentation.BaseAppViewModel
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.home.domain.useCases.GetCountriesUseCase
import ru.robilko.model.data.Country
import ru.robilko.model.data.GameResults
import java.util.Date
import javax.inject.Inject
import ru.robilko.core_ui.R as R_core_ui

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getGamesResultsUseCase: GetGamesResultsUseCase,
    private val getFavouriteTeamsUseCase: GetFavouriteTeamsUseCase,
    private val getFavouriteLeaguesUseCase: GetFavouriteLeaguesUseCase
) : BaseAppViewModel<HomeUiState, HomeUiEvent>() {
    private val _uiState = MutableStateFlow(HomeUiState())
    override val uiState: StateFlow<HomeUiState> = _uiState
    private var originalCountries: List<Country> = emptyList()
    private var allTodayGames: List<GameResults> = emptyList()
    private var favoriteTeamsIds: List<Int> = emptyList()
    private var favouriteLeaguesIds: List<Int> = emptyList()
    private var fetchGamesJob: Job? = null

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnTextChange -> {
                _uiState.update { it.copy(searchQuery = event.searchQuery) }
                renderCountries()
            }

            HomeUiEvent.OnScreenEntered -> onScreenEntered()
            HomeUiEvent.OnScreenExited -> onScreenExited()
            is HomeUiEvent.ChangeCheckedFavourites -> {
                _uiState.update { it.copy(checkedFavourite = event.value) }
                renderTodayGames()
            }

            HomeUiEvent.DetailsDialogDismiss ->
                _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.None) }

            is HomeUiEvent.GameCardClick ->
                _uiState.update { it.copy(detailsDialogState = GameDetailsDialogState.ShowData(event.game)) }
        }
    }

    init {
        observeOnFavouriteLeagues()
        observeOnFavouriteTeams()
        getTodayGames()
        getCountries()
    }

    private fun observeOnFavouriteLeagues() {
        viewModelScope.launch {
            getFavouriteLeaguesUseCase()
                .collectLatest { response ->
                    response.apply {
                        onSuccess { response ->
                            favouriteLeaguesIds = response.data.map { it.id }
                            renderTodayGames()
                        }
                    }
                }
        }
    }

    private fun observeOnFavouriteTeams() {
        viewModelScope.launch {
            getFavouriteTeamsUseCase()
                .collectLatest { response ->
                    response.apply {
                        onSuccess { response ->
                            favoriteTeamsIds = response.data.map { it.id }
                            renderTodayGames()
                        }
                    }
                }
        }
    }

    private fun renderTodayGames() {
        val games = if (_uiState.value.checkedFavourite) {
            allTodayGames.filter { game ->
                favoriteTeamsIds.any { id -> id == game.homeTeam.id || id == game.awayTeam.id }
                        || favouriteLeaguesIds.any { id -> id == game.league.id }
            }
        } else allTodayGames

        _uiState.update { it.copy(todayGames = games.toPersistentList()) }
    }

    private fun getTodayGames(isFetching: Boolean = false) {
        viewModelScope.launch {
            if (!isFetching) _uiState.update { it.copy(todayTabState = DataState.Loading) }

            getGamesResultsUseCase(date = Date().toStringDate(SERVER_DATE_PATTERN)).apply {
                onSuccess { response ->
                    allTodayGames = getFinalGamesResults(
                        newResults = response.data.sortedBy { game -> game.date },
                        isFetching = isFetching
                    )
                    renderTodayGames()
                    _uiState.update { it.copy(todayTabState = DataState.Success) }
                    checkNeedToFetchGameResults()
                }
                onFailure {
                    _uiState.update {
                        it.copy(
                            todayTabState = DataState.Error(
                                message = context.getString(
                                    R.string.getting_data_error
                                ),
                                onRetryAction = { getTodayGames(isFetching = isFetching) }
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getFinalGamesResults(
        newResults: List<GameResults>,
        isFetching: Boolean
    ): List<GameResults> {
        if (!isFetching) return newResults.toPersistentList()

        val currentList = allTodayGames.toMutableList()
        newResults.forEach { game ->
            val index = currentList.indexOfFirst { game.id == it.id }
            if (index != -1) currentList[index] = game
        }

        return currentList
    }

    private fun checkNeedToFetchGameResults() {
        if (fetchGamesJob == null || fetchGamesJob?.isCompleted == true) {
            fetchGamesJob = viewModelScope.launch {
                val hasGamesToCheck = allTodayGames.any { it.isPlayingNow }

                if (hasGamesToCheck) {
                    delay(GAMES_FETCH_TIMEOUT)
                    getTodayGames(isFetching = true)
                } else return@launch
            }
        }
    }

    private fun getCountries() {
        viewModelScope.launch {
            getCountriesUseCase().collect { response ->
                when (response) {
                    Response.Loading -> _uiState.update { it.copy(searchTabState = DataState.Loading) }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                searchTabState = DataState.Error(
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
                searchTabState = DataState.Success
            )
        }
    }

    private fun List<Country>.filterBySearchQuery(text: String): List<Country> {
        return if (text.length < SEARCH_QUERY_MIN_CHARS_COUNT) this else filter {
            it.name.lowercase().contains(text.lowercase().trim())
        }
    }

    private fun onScreenEntered() {
        checkNeedToFetchGameResults()
    }

    private fun onScreenExited() {
        fetchGamesJob?.cancel()
        fetchGamesJob = null
    }

    private companion object {
        const val SEARCH_QUERY_MIN_CHARS_COUNT = 3
        const val GAMES_FETCH_TIMEOUT = 60_000L
    }
}