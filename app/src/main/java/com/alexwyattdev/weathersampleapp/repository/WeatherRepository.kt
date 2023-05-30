package com.alexwyattdev.weathersampleapp.repository

import com.alexwyattdev.weathersampleapp.model.WeatherResponse
import com.alexwyattdev.weathersampleapp.service.WeatherApi
import javax.inject.Inject

// Repository to connect the viewModel to the Api Interface
class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {
    suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String): WeatherResponse {
        return weatherApi.getWeather(latitude, longitude, apiKey = apiKey)
    }

    suspend fun getWeatherByCityName(query: String, apiKey: String):
        WeatherResponse {
        return weatherApi.getWeatherByCityName(query, apiKey = apiKey)
    }
}
