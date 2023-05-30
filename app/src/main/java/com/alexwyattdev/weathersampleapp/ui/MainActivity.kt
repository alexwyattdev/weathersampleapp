package com.alexwyattdev.weathersampleapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexwyattdev.weathersampleapp.Constants.Companion.SEARCH_SCREEN
import com.alexwyattdev.weathersampleapp.Constants.Companion.WEATHER_SCREEN
import com.alexwyattdev.weathersampleapp.R
import com.alexwyattdev.weathersampleapp.model.ErrorType
import com.alexwyattdev.weathersampleapp.model.LocationDetails
import com.alexwyattdev.weathersampleapp.model.WeatherInfo
import com.alexwyattdev.weathersampleapp.ui.screens.SearchScreen
import com.alexwyattdev.weathersampleapp.ui.screens.WeatherScreen
import com.alexwyattdev.weathersampleapp.ui.theme.WeatherSampleAppTheme
import com.alexwyattdev.weathersampleapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherSampleAppTheme {
                // Defining variables
                val apiKey = stringResource(id = R.string.api_key)
                val viewModel: WeatherViewModel = viewModel()
                val weatherInfo by viewModel.weatherData.observeAsState()
                val errorPair by viewModel.errorType.observeAsState()
                val isLoading by viewModel.isLoading.observeAsState(initial = true)
                val context = LocalContext.current
                val locationObserver = remember {
                    Observer<LocationDetails> { location ->
                        viewModel.fetchWeatherByLocation(
                            location.latitude,
                            location.longitude,
                            apiKey,
                        )
                    }
                }

                // Main content holder
                WeatherAppContent(
                    weatherInfo,
                    errorPair,
                    isLoading,
                    onRefresh = {
                        viewModel.refresh(apiKey)
                    },
                    onSearchByCityName = { cityName, stateCode, countryCode ->
                        viewModel.fetchWeatherByCityName(cityName, stateCode, countryCode, apiKey)
                    },
                    onErrorShown = {
                        viewModel.resetError()
                    },
                )

                // Permission request result handling
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                ) { map ->
                    updateLocationAccessBasedOnPermissions(
                        map,
                        viewModel,
                        locationObserver,
                    )
                }

                // Permission request handling
                LaunchedEffect(true) {
                    checkAndRequestPermissions(
                        context,
                        viewModel,
                        locationObserver,
                        launcher,
                        apiKey,
                    )
                }
            }
        }
    }

    // If the app obtained all permissions, try retrieving weather info for the user's current
    // location
    private fun updateLocationAccessBasedOnPermissions(
        map: Map<String, Boolean>,
        viewModel: WeatherViewModel,
        locationObserver: Observer<LocationDetails>,
    ) {
        if (map.all { it.value }) {
            viewModel.location.observe(this@MainActivity, locationObserver)
            viewModel.startLocationUpdates()
        } else {
            viewModel.permissionWasAsked()
        }
    }

    // Check if the app has the required permissions, if yes, retrieve the user's current
    // location, if no then check if the user was already asked to grant permissions, if no, ask
    // for permissions, if yes, then try loading the last searched city's weather info

    private fun checkAndRequestPermissions(
        context: Context,
        viewModel: WeatherViewModel,
        locationObserver: Observer<LocationDetails>,
        launcher: ActivityResultLauncher<Array<String>>,
        apiKey: String,
    ) {
        if (hasPermissions(context)) {
            viewModel.permissionWasAsked()
            viewModel.location.observe(this@MainActivity, locationObserver)
            viewModel.startLocationUpdates()
        } else {
            if (viewModel.permissionWasAskedBefore()) {
                viewModel.fetchWeatherByCityName(null, apiKey = apiKey)
            } else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
        }
    }

    // Check if permissions were granted
    private fun hasPermissions(context: Context): Boolean {
        return (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            )
    }
}

@Composable
fun WeatherAppContent(
    weatherInfo: WeatherInfo?,
    errorPair: Pair<ErrorType, String?>?,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSearchByCityName: (String, String, String) -> Unit,
    onErrorShown: () -> Unit,
) {
    // Component to handle the navigation between the 2 screens
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController, startDestination = WEATHER_SCREEN) {
            composable(WEATHER_SCREEN) {
                WeatherScreen(
                    weatherInfo = weatherInfo,
                    errorType = errorPair,
                    isLoading = isLoading,
                    navigateToSearch = { navController.navigate(SEARCH_SCREEN) },
                    onRefresh = { onRefresh() },
                    onErrorShown = { onErrorShown() },
                )
            }
            composable(SEARCH_SCREEN) {
                SearchScreen(
                    onSearch = { cityName, stateCode, countryCode ->
                        navController.popBackStack()
                        onSearchByCityName(cityName, stateCode, countryCode)
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    WeatherSampleAppTheme {
        WeatherAppContent(
            WeatherInfo(),
            null,
            false,
            onRefresh = {},
            onSearchByCityName = { _: String, _: String, _: String -> },
            onErrorShown = {},
        )
    }
}
