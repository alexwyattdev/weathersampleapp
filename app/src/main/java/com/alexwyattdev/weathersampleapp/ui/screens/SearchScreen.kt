package com.alexwyattdev.weathersampleapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexwyattdev.weathersampleapp.R
import com.alexwyattdev.weathersampleapp.model.DropDownItem
import com.alexwyattdev.weathersampleapp.model.States
import com.alexwyattdev.weathersampleapp.ui.theme.WeatherSampleAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onSearch: (String, String, String) -> Unit, onBack: () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.onPrimary
    var cityName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val emptySelectionString = "-"
    var selectedState by remember { mutableStateOf(DropDownItem(emptySelectionString, "")) }
    var selectedCountry by remember { mutableStateOf(DropDownItem(emptySelectionString, "")) }
    var stateSelectorExpanded by remember { mutableStateOf(false) }
    var countrySelectorExpanded by remember { mutableStateOf(false) }
    val states = remember { mutableStateOf(States.usStates) }
    val countries = remember {
        mutableStateOf(
            listOf(
                DropDownItem(name = emptySelectionString, code = ""),
                *Locale.getISOCountries().map { code ->
                    val locale = Locale("", code)
                    DropDownItem(
                        code = code,
                        name = locale.displayCountry,
                    )
                }.toTypedArray(),
            ),
        )
    }

    fun handleSearch() {
        if (cityName.isNotEmpty() && cityName.length > 2) {
            showError = false
            onSearch(cityName, selectedState.code, selectedCountry.code)
        } else {
            showError = true
        }
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarColor.luminance() > 0.5f,
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.search),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.onPrimary),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = cityName,
                        onValueChange = {
                            cityName = it
                            showError = it.isNotEmpty() && it.length <= 2
                        },
                        placeholder = { Text(stringResource(id = R.string.city_name)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            handleSearch()
                        }),
                        shape = RoundedCornerShape(4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            textColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            errorLabelColor = Color.Red,
                        ),
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = if (showError) stringResource(id = R.string.city_name_too_short) else "",
                    color = Color.Red,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                ) {
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = stateSelectorExpanded,
                        onExpandedChange = {
                            stateSelectorExpanded = !stateSelectorExpanded
                        },
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            value = selectedState.name.takeUnless { it == emptySelectionString }
                                ?: "",
                            onValueChange = {},
                            placeholder = { Text(stringResource(id = R.string.select_state)) },
                            singleLine = true,
                            readOnly = true,
                            shape = RoundedCornerShape(4.dp),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                textColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                errorLabelColor = Color.Red,
                            ),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateSelectorExpanded)
                            },
                        )

                        ExposedDropdownMenu(
                            modifier = Modifier
                                .exposedDropdownSize(true)
                                .background(color = MaterialTheme.colorScheme.onPrimary),
                            expanded = stateSelectorExpanded,
                            onDismissRequest = { stateSelectorExpanded = false },
                        ) {
                            states.value.forEach { item ->
                                DropdownMenuItem(
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    text = { Text(text = item.name) },
                                    onClick = {
                                        selectedState = item
                                        stateSelectorExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                ) {
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = countrySelectorExpanded,
                        onExpandedChange = {
                            countrySelectorExpanded = !countrySelectorExpanded
                        },
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            value = selectedCountry.name.takeUnless { it == emptySelectionString }
                                ?: "",
                            onValueChange = {},
                            placeholder = { Text(stringResource(id = R.string.select_country)) },
                            singleLine = true,
                            readOnly = true,
                            shape = RoundedCornerShape(4.dp),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                textColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                errorLabelColor = Color.Red,
                            ),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = countrySelectorExpanded)
                            },
                        )

                        ExposedDropdownMenu(
                            modifier = Modifier
                                .exposedDropdownSize(true)
                                .background(color = MaterialTheme.colorScheme.onPrimary),
                            expanded = countrySelectorExpanded,
                            onDismissRequest = { countrySelectorExpanded = false },
                        ) {
                            countries.value.forEach { item ->
                                DropdownMenuItem(
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    text = { Text(text = item.name) },
                                    onClick = {
                                        selectedCountry = item
                                        countrySelectorExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(52.dp)
                        .fillMaxWidth(),
                    onClick = {
                        if (cityName.isNotBlank() && cityName.length > 2) {
                            onSearch(cityName, selectedState.code, selectedCountry.code)
                        } else {
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        stringResource(id = R.string.search),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    WeatherSampleAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
            SearchScreen(onSearch = { _: String, _: String, _: String -> }, onBack = {})
        }
    }
}
