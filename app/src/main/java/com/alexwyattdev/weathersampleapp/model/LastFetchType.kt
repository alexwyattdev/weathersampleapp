package com.alexwyattdev.weathersampleapp.model

// Enum class to be able to handle the different weather fetching scenarios
// when the user does a pull to refresh action
enum class LastFetchType {
    CITY,
    LOCATION,
}
