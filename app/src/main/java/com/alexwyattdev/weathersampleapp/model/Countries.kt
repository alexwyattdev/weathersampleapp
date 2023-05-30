package com.alexwyattdev.weathersampleapp.model

import com.alexwyattdev.weathersampleapp.Constants.Companion.emptySelectionString
import java.util.Locale

// Object to get the list of all countries and their ISO-3166 codes
// and adding an empty item so the user can do a "deselect"
// With an implementation where the user can type into the box, this extra item
// would not be required
object Countries {
    val countries = listOf(
        DropDownItem(name = emptySelectionString, code = ""),
        *Locale.getISOCountries().map { code ->
            val locale = Locale("", code)
            DropDownItem(
                code = code,
                name = locale.displayCountry,
            )
        }.toTypedArray(),
    )
    val usCountry = countries.first { it.code == "US" }
}
