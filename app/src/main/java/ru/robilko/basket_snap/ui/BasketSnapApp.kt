package ru.robilko.basket_snap.ui

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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import ru.robilko.basket_snap.R
import ru.robilko.basket_snap.navigation.AppNavHost
import ru.robilko.basket_snap.navigation.TopLevelDestination

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
            if (appState.needToShowTopBar) {
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
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            AppBottomBar(
                currentDestination = appState.currentDestination,
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
    currentDestination: NavDestination?,
    onClick: (TopLevelDestination) -> Unit
) {
    val items = remember { TopLevelDestination.entries }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        items.forEach { topLevelDestination ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == topLevelDestination.route } == true,
                onClick = { onClick(topLevelDestination) },
                icon = {
                    Icon(
                        imageVector = topLevelDestination.menuIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = topLevelDestination.titleResId))
                }
            )
        }
    }
}