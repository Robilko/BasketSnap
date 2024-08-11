package ru.robilko.home.presentation

import AppOutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import ru.robilko.core_ui.presentation.DataState
import ru.robilko.core_ui.presentation.components.AppCard
import ru.robilko.core_ui.presentation.components.AppText
import ru.robilko.core_ui.presentation.components.ErrorScreen
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.core_ui.utils.bounceClick
import ru.robilko.home.R
import ru.robilko.model.data.Continent
import ru.robilko.model.data.Country
import ru.robilko.model.R as R_model

@Composable
internal fun HomeRoute(
    onTopBarTitleChange: (resId: Int) -> Unit,
    onNavigateToLeagues: (Country) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    LaunchedEffect(Unit) { onTopBarTitleChange(R.string.home_page_title) }
    HomeScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        onNavigateToLeagues = onNavigateToLeagues,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToLeagues: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (uiState.dataState) {
            DataState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            is DataState.Error -> {
                ErrorScreen(
                    text = uiState.dataState.message,
                    modifier = Modifier.fillMaxSize(),
                    onRetryButtonClick = uiState.dataState.onRetryAction
                )
            }

            is DataState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    searchField(
                        query = uiState.searchQuery,
                        onChange = { onEvent(HomeUiEvent.OnTextChange(it)) }
                    )
                    if (uiState.countries.isNotEmpty()) {
                        countriesList(
                            countries = uiState.countries,
                            onClick = onNavigateToLeagues
                        )
                    }
                    if (uiState.continents.isNotEmpty()) {
                        continentsList(
                            countries = uiState.continents,
                            onClick = onNavigateToLeagues
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.searchField(query: String, onChange: (String) -> Unit) {
    item {
        AppOutlinedTextField(
            title = stringResource(id = R.string.search_field_title),
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
}

private fun LazyListScope.countriesList(
    countries: PersistentList<Country>,
    onClick: (Country) -> Unit
) {
    item { ListTitle(text = stringResource(R.string.countries_title)) }
    items(countries, key = { it.name }) { country ->
        AppCard(
            modifier = Modifier.fillMaxWidth(),
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