package com.alexwyattdev.weathersampleapp.service

import com.alexwyattdev.weathersampleapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") apiKey: String,
    ): WeatherResponse

    @GET("data/2.5/weather")
    suspend fun getWeatherByCityName(
        @Query("q") query: String,
        @Query("units") units: String = "imperial",
        @Query("appid") apiKey: String,
    ): WeatherResponse
}
