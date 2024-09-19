package ru.robilko.basket_snap

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val appState = rememberBasketSnapAppState(appConfigRepository)

            val darkTheme = appState.isDarkTheme
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle =
                    SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(lightScrim, darkScrim) { darkTheme }
                )
                onDispose {}
            }

            BasketSnapTheme(isDarkTheme = appState.isDarkTheme) {
                BasketSnapApp(appState)
            }
        }
    }
}

private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)