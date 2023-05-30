package com.alexwyattdev.weathersampleapp.model

// Given time, this enum class could be expanded to handle different errors such as HTTP 404
enum class ErrorType {
    ERROR,
    INVALID_CITY_NAME,
    INVALID_LOCATION_DATA,
}
