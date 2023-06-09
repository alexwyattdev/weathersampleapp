package com.alexwyattdev.weathersampleapp.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Store in the SharedPreferences the following:
// the last searched city, state, country and if the
// permission dialog was already shown to
// the user, all in the SharedPreferences
// As this project is smaller, there is no need to implement an SQL Database
// solution
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

    fun saveCityName(cityName: String) {
        sharedPreferences.edit().putString("cityName", cityName).apply()
    }

    fun getCityName(): String? {
        return sharedPreferences.getString("cityName", null)
    }

    fun saveStateCode(stateCode: String) {
        sharedPreferences.edit().putString("stateCode", stateCode).apply()
    }

    fun getStateCode(): String? {
        return sharedPreferences.getString("stateCode", null)
    }

    fun saveCountryCode(countryCode: String) {
        sharedPreferences.edit().putString("countryCode", countryCode).apply()
    }

    fun getCountryCode(): String? {
        return sharedPreferences.getString("countryCode", null)
    }

    fun permissionWasAsked() {
        sharedPreferences.edit().putBoolean("permissionWasAsked", true).apply()
    }

    fun permissionWasAskedBefore(): Boolean {
        return sharedPreferences.getBoolean("permissionWasAsked", false)
    }
}
