package ru.robilko.basket_snap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.robilko.basket_snap.ui.BasketSnapApp
import ru.robilko.basket_snap.ui.rememberBasketSnapAppState
import ru.robilko.core_ui.theme.BasketSnapTheme
import ru.robilko.settings.domain.repo.AppConfigRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appConfigRepository: AppConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberBasketSnapAppState(appConfigRepository)

            BasketSnapTheme(isDarkTheme = appState.isDarkTheme) {
                BasketSnapApp(appState)
            }
        }
    }
}