package com.alexwyattdev.weathersampleapp.viewmodel

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexwyattdev.weathersampleapp.model.ErrorType
import com.alexwyattdev.weathersampleapp.model.LastFetchType
import com.alexwyattdev.weathersampleapp.model.LocationDetails
import com.alexwyattdev.weathersampleapp.model.WeatherInfo
import com.alexwyattdev.weathersampleapp.model.WeatherResponse
import com.alexwyattdev.weathersampleapp.model.toWeatherInfo
import com.alexwyattdev.weathersampleapp.repository.WeatherRepository
import com.alexwyattdev.weathersampleapp.util.PreferencesManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

// The viewmodel to handle the API requests, and some location management
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val preferencesManager: PreferencesManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherInfo>()
    val weatherData: LiveData<WeatherInfo> = _weatherData

    private val _errorType = MutableLiveData<Pair<ErrorType, String?>>()
    val errorType: LiveData<Pair<ErrorType, String?>> = _errorType

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _location = MutableLiveData<LocationDetails>()
    val location: LiveData<LocationDetails> = _location

    private var lastFetchType = LastFetchType.CITY

    private val locationRequest = buildLocationRequest()
    private val locationCallback = buildLocationCallback()

    // User activated the pull to refresh function, depending on what was the last fetch type
    // the app reloads the weather information
    fun refresh(apiKey: String) {
        when (lastFetchType) {
            LastFetchType.CITY -> {
                val cityName = getCityName()
                if (cityName != null) {
                    fetchWeatherByCityName(cityName, apiKey = apiKey)
                } else {
                    _errorType.value = Pair(ErrorType.INVALID_CITY_NAME, null)
                }
            }

            LastFetchType.LOCATION -> {
                val location = _location.value
                if (location != null) {
                    fetchWeatherByLocation(
                        location.latitude,
                        location.longitude,
                        apiKey,
                    )
                } else {
                    _errorType.value = Pair(ErrorType.INVALID_LOCATION_DATA, null)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun fetchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String) {
        fetchWeatherData(LastFetchType.LOCATION) {
            weatherRepository.getWeather(latitude, longitude, apiKey)
        }
    }

    // If there is no cityName given, check if there is one saved in SharedPreferences,
    // If there is no saved city name, then the app should not stay in the loading state
    fun fetchWeatherByCityName(
        cityName: String?,
        stateCode: String = "",
        countryCode: String = "",
        apiKey: String,
    ) {
        val savedCityName = cityName ?: getCityName()
        savedCityName?.let {
            saveCityName(it)
            saveStateCode(stateCode)
            saveCountryCode(countryCode)
            val query = buildQuery(it)
            fetchWeatherData(LastFetchType.CITY) {
                weatherRepository.getWeatherByCityName(query, apiKey)
            }
        } ?: kotlin.run {
            _isLoading.value = false
        }
    }

    fun getCityName(): String? = preferencesManager.getCityName()
    fun permissionWasAsked() = preferencesManager.permissionWasAsked()
    fun permissionWasAskedBefore(): Boolean = preferencesManager.permissionWasAskedBefore()

    // Avoid getting error Snackbar displays by resetting the error if the user already has seen it
    fun resetError() {
        _errorType.value = null
    }

    private fun buildLocationRequest() =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateDistanceMeters(1f)
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setWaitForAccurateLocation(false)
            .build()

    private fun buildLocationCallback() = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.lastOrNull()?.let {
                _location.value = LocationDetails(it.latitude, it.longitude)
            }
            stopLocationUpdates()
        }
    }

    private fun getStateCode(): String? = preferencesManager.getStateCode()
    private fun getCountryCode(): String? = preferencesManager.getCountryCode()

    // Build the query for the API so it can handle if the user only searches by city, or
    // by city + state code, or by city + state code + country code, or by city + country code
    private fun buildQuery(cityName: String): String {
        val stateCode = getStateCode()
        val countryCode = getCountryCode()
        return "$cityName,${stateCode.orEmpty()},${countryCode.orEmpty()}".trimEnd(',')
    }

    // Load the weather data in a separate thread
    private fun fetchWeatherData(
        currentFetchType: LastFetchType,
        fetchAction: suspend () ->
        WeatherResponse,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _weatherData.value = fetchAction().toWeatherInfo()
                lastFetchType = currentFetchType
            } catch (e: HttpException) {
                _errorType.value = Pair(ErrorType.ERROR, e.message)
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveCityName(cityName: String) {
        preferencesManager.saveCityName(cityName)
    }

    private fun saveStateCode(stateCode: String) {
        preferencesManager.saveStateCode(stateCode)
    }

    private fun saveCountryCode(countryCode: String) {
        preferencesManager.saveCountryCode(countryCode)
    }
}
