package com.alexwyattdev.weathersampleapp.util

// Convert the Wind direction degree to direction String
fun Int.toWindDirection(): String {
    val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((this + 22.5) / 45).toInt() % 8
    return directions[index]
}

// Get the full url for the dynamic weather icon
fun String.toIconUrl(): String {
    return "https://openweathermap.org/img/wn/$this@2x.png"
}
