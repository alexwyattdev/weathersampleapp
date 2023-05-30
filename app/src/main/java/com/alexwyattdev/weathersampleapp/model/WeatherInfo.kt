package com.alexwyattdev.weathersampleapp.model

import com.alexwyattdev.weathersampleapp.util.toIconUrl
import com.alexwyattdev.weathersampleapp.util.toWindDirection

data class WeatherInfo(
    val cityName: String? = null,
    val temperature: String? = null,
    val humidity: String? = null,
    val description: String? = null,
    val windSpeed: String? = null,
    val windDirection: String? = null,
    val icon: String? = null,
    val feelsLike: String? = null,
    val tempMin: String? = null,
    val tempMax: String? = null,
    val rain1h: String? = null,
    val rain3h: String? = null,
)

fun WeatherResponse.toWeatherInfo(): WeatherInfo {
    val cityName = name
    val temperature = main?.temp?.toString()
    val humidity = main?.humidity?.toString()
    val weather = weather?.get(0)
    val weatherDescription = weather?.description
    val windSpeed = wind?.speed?.toString()
    val windDirection = wind?.deg?.toWindDirection()
    val icon = weather?.icon?.toIconUrl()
    val feelsLike = main?.feelsLike?.toString()
    val tempMin = main?.tempMin?.toString()
    val tempMax = main?.tempMax?.toString()
    val rain1h = rain?.rain1h?.toString()
    val rain3h = rain?.rain3h?.toString()

    return WeatherInfo(
        cityName = cityName,
        temperature = temperature,
        humidity = humidity,
        description = weatherDescription,
        windSpeed = windSpeed,
        windDirection = windDirection,
        icon = icon,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        rain1h = rain1h,
        rain3h = rain3h,
    )
}
