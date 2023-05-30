package com.alexwyattdev.weathersampleapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexwyattdev.weathersampleapp.R
import com.alexwyattdev.weathersampleapp.model.ErrorType
import com.alexwyattdev.weathersampleapp.model.WeatherInfo
import com.alexwyattdev.weathersampleapp.ui.theme.WeatherSampleAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    weatherInfo: WeatherInfo?,
    errorType: Pair<ErrorType, String?>?,
    isLoading: Boolean,
    navigateToSearch: () -> Unit,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val defaultErrorMessage = stringResource(id = R.string.error_unexpected)
    val defaultSnackBarResponse = stringResource(id = android.R.string.ok)
    val pullRefreshState = rememberPullRefreshState(isLoading, { onRefresh() })
    val invalidCityError = stringResource(id = R.string.error_invalid_city)
    val invalidLocationError = stringResource(id = R.string.error_invalid_location)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarColor.luminance() > 0.5f,
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.weather_title),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                navigationIcon = { },
                actions = {
                    IconButton(onClick = navigateToSearch) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.primary),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.weight(1f).padding(bottom = 64.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    WeatherInfoDisplay(weatherInfo = weatherInfo)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    TemperatureDetails(weatherInfo = weatherInfo)
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    WeatherDetails(weatherInfo = weatherInfo)
                }
            }
        }
        errorType?.let {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = when (it.first) {
                        ErrorType.ERROR -> it.second ?: defaultErrorMessage
                        ErrorType.INVALID_CITY_NAME -> invalidCityError
                        ErrorType.INVALID_LOCATION_DATA -> invalidLocationError
                    },
                    actionLabel = defaultSnackBarResponse,
                )
                onErrorShown()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ) {
            PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun WeatherInfoDisplay(weatherInfo: WeatherInfo?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            modifier = Modifier.size(128.dp),
            model = weatherInfo?.icon,
            contentDescription = stringResource(id = R.string.weather_icon),
            placeholder = painterResource(R.drawable.ic_unknown),
            error = painterResource(R.drawable.ic_unknown),
            contentScale = ContentScale.FillBounds,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.temperature, weatherInfo?.temperature ?: ""),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = weatherInfo?.cityName ?: "",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = getCurrentDate(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        weatherInfo?.rain1h?.let {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.rain_1h, it),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        weatherInfo?.rain3h?.let {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.rain_3h, it),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun TemperatureDetails(weatherInfo: WeatherInfo?) {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.feelsLike,
            labelResourceId = R.string.temperature_feels,
            stringResourceId = R.string.temperature,
        )

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp,
        )

        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.tempMin,
            labelResourceId = R.string.temperature_min,
            stringResourceId = R.string.temperature,
        )

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp,
        )

        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.tempMax,
            labelResourceId = R.string.temperature_max,
            stringResourceId = R.string.temperature,
        )
    }
}

@Composable
fun WeatherDetails(weatherInfo: WeatherInfo?) {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.windSpeed,
            labelResourceId = R.string.wind_speed,
            stringResourceId = R.string.wind,
        )

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp,
        )

        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.windDirection,
            labelResourceId = R.string.wind_direction,
        )

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp,
        )

        WeatherDetailText(
            modifier = Modifier.weight(1f),
            text = weatherInfo?.humidity,
            labelResourceId = R.string.humidity_label,
            stringResourceId = R.string.humidity,
        )
    }
}

@Composable
fun WeatherDetailText(
    modifier: Modifier,
    text: String?,
    stringResourceId: Int? = null,
    labelResourceId: Int,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = labelResourceId),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        text?.let {
            Text(
                text = stringResourceId?.let { resourceId ->
                    stringResource(id = resourceId, it)
                } ?: kotlin.run {
                    it
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun getCurrentDate(): String {
    val date = Date()
    val format = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val weatherInfo = WeatherInfo(
        cityName = "New York",
        temperature = "72.5",
        description = "Sunny",
        humidity = "60",
        windSpeed = "10",
        windDirection = "NW",
        icon = "01d",
        feelsLike = "71",
        tempMin = "65",
        tempMax = "80",
        rain1h = "12",
        rain3h = "14",
    )
    WeatherSampleAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
            WeatherScreen(
                weatherInfo,
                Pair(ErrorType.ERROR, null),
                false,
                navigateToSearch = {},
                onRefresh = {},
                onErrorShown = {},
            )
        }
    }
}
