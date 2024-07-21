package ru.robilko.basket_snap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.robilko.core_ui.theme.BasketSnapTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen() {
    val navController = rememberNavController()
    var currentDestination by remember { mutableStateOf<Destination>(Destination.FirstScreen) }

    BasketSnapTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = currentDestination.titleResId))
                    },
                    navigationIcon = {
                        if (currentDestination != Destination.FirstScreen) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = { AppBottomBar(navController, currentDestination) },
            content = { innerPadding ->
                AppNavHost(
                    paddingValues = innerPadding,
                    navHostController = navController
                ) { destination -> currentDestination = destination }
            }
        )
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController, currentDestination: Destination) {
    val items = remember { Destination.getBottomBarItems() }
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination == screen,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.menuIcon ?: Icons.Default.Close,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = screen.titleResId))
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL)
@Composable
fun MainScreenPreview() {
    MainScreen()
}