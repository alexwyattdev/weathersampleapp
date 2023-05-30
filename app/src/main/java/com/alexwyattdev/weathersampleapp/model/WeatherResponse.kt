package com.alexwyattdev.weathersampleapp.model

import com.google.gson.annotations.SerializedName

// The API response
data class WeatherResponse(
    @SerializedName("weather")
    val weather: List<Weather>?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("wind")
    val wind: Wind?,
    @SerializedName("rain")
    val rain: Rain?,
    @SerializedName("name")
    val name: String?,
)

data class Weather(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
)

data class Main(
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @SerializedName("temp_min")
    val tempMin: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    @SerializedName("humidity")
    val humidity: Int?,
)

data class Wind(
    @SerializedName("speed")
    val speed: Double?,
    @SerializedName("deg")
    val deg: Int?,
)

data class Rain(
    @SerializedName("1h")
    val rain1h: Double?,
    @SerializedName("3h")
    val rain3h: Double?,
)
