package com.alexwyattdev.weathersampleapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeatherSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Timber logging
        Timber.plant(object : Timber.DebugTree() {})
    }
}
