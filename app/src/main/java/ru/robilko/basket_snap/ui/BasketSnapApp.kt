package ru.robilko.basket_snap.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.robilko.basket_snap.R
import ru.robilko.basket_snap.navigation.AppNavHost
import ru.robilko.basket_snap.navigation.TopLevelDestination

private const val TOP_BAR_ANIM_DURATION = 1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BasketSnapApp(
    appState: BasketSnapAppState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var topBarTitle by rememberSaveable { mutableIntStateOf(R.string.app_name) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(
                visible = appState.needToShowTopBar,
                enter = expandVertically(
                    tween(durationMillis = TOP_BAR_ANIM_DURATION),
                    initialHeight = { it / 3 }) + fadeIn(tween(TOP_BAR_ANIM_DURATION)),
                exit = shrinkVertically(
                    tween(durationMillis = TOP_BAR_ANIM_DURATION),
                    targetHeight = { it / 3 }) + fadeOut(tween(TOP_BAR_ANIM_DURATION))
            ) {
                TopAppBar(
                    title = { Text(text = stringResource(id = topBarTitle)) },
                    navigationIcon = {
                        if (appState.needToShowTopBarBackButton) {
                            IconButton(onClick = appState::navigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        } else Spacer(modifier = Modifier.size(48.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = Color.Black,
                        titleContentColor = Color.Black.copy(alpha = 0.6f)
                    ),
                    expandedHeight = 40.dp
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                topLevelDestinations = appState.topLevelDestinations,
                currentTopLevelDestination = appState.currentTopLevelDestination,
                onClick = { appState.navigateToTopLevelDestination(it) }
            )
        },
        snackbarHost = {

        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppNavHost(
                    appState = appState,
                    onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short
                        ) == SnackbarResult.ActionPerformed
                    },
                    onTopBarTitleChange = { topBarTitle = it }
                )
            }
        }
    )
}

@Composable
private fun AppBottomBar(
    topLevelDestinations: List<TopLevelDestination>,
    currentTopLevelDestination: TopLevelDestination?,
    onClick: (TopLevelDestination) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        topLevelDestinations.forEach { topLevelDestination ->
            NavigationBarItem(
                selected = currentTopLevelDestination == topLevelDestination,
                onClick = { onClick(topLevelDestination) },
                icon = {
                    Icon(
                        imageVector = topLevelDestination.menuIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = topLevelDestination.titleResId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    indicatorColor = Color(0xFFE0E0E0),
                    unselectedIconColor = Color.Black.copy(alpha = 0.6f),
                    unselectedTextColor = Color.Black.copy(alpha = 0.6f)
                )
            )
        }
    }
}