package ru.robilko.home.presentation

import AppOutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import ru.robilko.base_games.presentation.GameDetailsDialog
import ru.robilko.base_games.presentation.GameDetailsDialogState
import ru.robilko.base_games.presentation.GamesList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.home.R
import ru.robilko.model.data.Continent
import ru.robilko.model.data.Country
import ru.robilko.model.data.GameResults
import ru.robilko.model.R as R_model

@Composable
internal fun HomeRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagues: (Country) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.home_page_title) }
    HomeScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToLeagues = onNavigateToLeagues,
        onNavigateToTeamDetails = onNavigateToTeamDetails,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToLeagues: (Country) -> Unit,
    onNavigateToTeamDetails: (teamId: Int, leagueId: Int, season: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { HomeTabs.entries.size }
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    onEvent(HomeUiEvent.OnScreenEntered)
                }

                Lifecycle.Event.ON_STOP -> {
                    onEvent(HomeUiEvent.OnScreenExited)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = BasketSnapTheme.colors.primaryText
                )
            }
        ) {
            HomeTabs.entries.forEachIndexed { index, tabItem ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    content = {
                        AppText(
                            text = stringResource(id = tabItem.titleResId),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                )
            }
        }

        HorizontalPager(state = pagerState, userScrollEnabled = true) { pageIndex ->
            when (pageIndex) {
                HomeTabs.TODAY.ordinal -> {
                    TodayGamesTab(
                        state = uiState.todayTabState,
                        games = uiState.todayGames,
                        detailsDialogState = uiState.detailsDialogState,
                        checkedFavourites = uiState.checkedFavourite,
                        onDialogDismiss = { onEvent(HomeUiEvent.DetailsDialogDismiss) },
                        onCardClick = { onEvent(HomeUiEvent.GameCardClick(it)) },
                        onTeamClick = onNavigateToTeamDetails,
                        onCheckedFavouritesChanged = {
                            onEvent(
                                HomeUiEvent.ChangeCheckedFavourites(
                                    it
                                )
                            )
                        }
                    )
                }

                HomeTabs.SEARCH.ordinal -> {
                    SearchTab(
                        state = uiState.searchTabState,
                        searchQuery = uiState.searchQuery,
                        continents = uiState.continents,
                        countries = uiState.countries,
                        onSearchQueryChanged = { onEvent(HomeUiEvent.OnTextChange(it)) },
                        onNavigateToLeagues = onNavigateToLeagues
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayGamesTab(
    state: DataState,
    detailsDialogState: GameDetailsDialogState,
    games: PersistentList<GameResults>,
    checkedFavourites: Boolean,
    onCheckedFavouritesChanged: (Boolean) -> Unit,
    onDialogDismiss: () -> Unit,
    onCardClick: (GameResults) -> Unit,
    onTeamClick: (teamId: Int, leagueId: Int, season: String?) -> Unit
) {
    detailsDialogState.let {
        if (it is GameDetailsDialogState.ShowData) {
            GameDetailsDialog(
                gameResults = it.gameResults,
                onDismiss = onDialogDismiss
            )
        }
    }

    when (state) {
        is DataState.Error -> {
            ErrorScreen(
                text = state.message,
                modifier = Modifier.fillMaxSize(),
                onRetryButtonClick = state.onRetryAction
            )
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AppText(
                        text = stringResource(R.string.only_favorites_switch_text),
                        fontStyle = FontStyle.Italic,
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = checkedFavourites,
                        onCheckedChange = onCheckedFavouritesChanged
                    )
                }
                GamesList(
                    isLoading = state is DataState.Loading,
                    games = games,
                    onClick = onCardClick,
                    onTeamClick = onTeamClick,
                    modifier = Modifier.fillMaxSize(),
                    emptyMessageResId = if (checkedFavourites) R.string.empty_favourites_games
                    else R.string.empty_today_games
                )
            }
        }
    }
}

@Composable
private fun SearchTab(
    state: DataState,
    searchQuery: String,
    continents: PersistentList<Country>,
    countries: PersistentList<Country>,
    onSearchQueryChanged: (String) -> Unit,
    onNavigateToLeagues: (Country) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            DataState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            is DataState.Error -> {
                ErrorScreen(
                    text = state.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetryButtonClick = state.onRetryAction
                )
            }

            is DataState.Success -> {
                Column {
                    SearchField(
                        query = searchQuery,
                        onChange = onSearchQueryChanged,
                        modifier = Modifier
                            .padding(16.dp)
                            .testTag("HomeSearchField")
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                    ) {
                        if (continents.isNotEmpty()) {
                            continentsList(
                                countries = continents,
                                onClick = onNavigateToLeagues
                            )
                        }
                        if (countries.isNotEmpty()) {
                            countriesList(
                                countries = countries,
                                onClick = onNavigateToLeagues
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onChange: (String) -> Unit, modifier: Modifier = Modifier) {
    AppOutlinedTextField(
        title = stringResource(id = R.string.search_field_title),
        modifier = modifier,
        value = query,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        trailingIcon = {
            if (query.isNotEmpty()) {
                Image(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.bounceClick { onChange("") },
                    colorFilter = ColorFilter.tint(BasketSnapTheme.colors.primaryText)
                )
            } else {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(BasketSnapTheme.colors.primaryText)
                )
            }
        },
        onChange = onChange
    )
}

private fun LazyListScope.countriesList(
    countries: PersistentList<Country>,
    onClick: (Country) -> Unit
) {
    item { ListTitle(text = stringResource(R.string.countries_title)) }
    items(countries, key = { it.name }) { country ->
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("CountryCard"),
            contentPadding = PaddingValues(8.dp),
            onClick = { onClick(country) }) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (country.flagUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(country.flagUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .error(R_model.drawable.ic_flag_placeholder)
                            .placeholder(R_model.drawable.ic_flag_placeholder)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .networkCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
                AppText(text = country.name, modifier = Modifier.weight(1f))
                AppText(text = country.code)
            }
        }
    }
}

private fun LazyListScope.continentsList(
    countries: PersistentList<Country>,
    onClick: (Country) -> Unit
) {
    item { ListTitle(text = stringResource(R.string.continents_title)) }
    items(countries) { country ->
        AppCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            onClick = { onClick(country) }) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    imageVector = ImageVector.vectorResource(id = Continent.getIconResId(country.name)),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillWidth
                )
                AppText(text = country.name, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ListTitle(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp), contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = BasketSnapTheme.colors.secondaryText
        )
    }
}