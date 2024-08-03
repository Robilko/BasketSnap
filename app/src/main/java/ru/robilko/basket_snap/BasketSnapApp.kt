package ru.robilko.basket_snap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BasketSnapApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}