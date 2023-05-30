package com.alexwyattdev.weathersampleapp.model

import com.alexwyattdev.weathersampleapp.Constants.Companion.emptySelectionString

// Object to get the list of all USA States and their ISO-3166 codes
// and adding an empty item so the user can do a "deselect"
// With an implementation where the user can type into the box, this extra item
// would not be required
object States {
    val usStates =
        listOf(
            DropDownItem(name = emptySelectionString, code = ""),
            DropDownItem(name = "Alabama", code = "US_AL"),
            DropDownItem(name = "Alaska", code = "US_AK"),
            DropDownItem(name = "Arizona", code = "US_AZ"),
            DropDownItem(name = "Arkansas", code = "US_AR"),
            DropDownItem(name = "California", code = "US_CA"),
            DropDownItem(name = "Colorado", code = "US_CO"),
            DropDownItem(name = "Connecticut", code = "US_CT"),
            DropDownItem(name = "Delaware", code = "US_DE"),
            DropDownItem(name = "Florida", code = "US_FL"),
            DropDownItem(name = "Georgia", code = "US_GA"),
            DropDownItem(name = "Hawaii", code = "US_HI"),
            DropDownItem(name = "Idaho", code = "US_ID"),
            DropDownItem(name = "Illinois", code = "US_IL"),
            DropDownItem(name = "Indiana", code = "US_IN"),
            DropDownItem(name = "Iowa", code = "US_IA"),
            DropDownItem(name = "Kansas", code = "US_KS"),
            DropDownItem(name = "Kentucky", code = "US_KY"),
            DropDownItem(name = "Louisiana", code = "US_LA"),
            DropDownItem(name = "Maine", code = "US_ME"),
            DropDownItem(name = "Maryland", code = "US_MD"),
            DropDownItem(name = "Massachusetts", code = "US_MA"),
            DropDownItem(name = "Michigan", code = "US_MI"),
            DropDownItem(name = "Minnesota", code = "US_MN"),
            DropDownItem(name = "Mississippi", code = "US_MS"),
            DropDownItem(name = "Missouri", code = "US_MO"),
            DropDownItem(name = "Montana", code = "US_MT"),
            DropDownItem(name = "Nebraska", code = "US_NE"),
            DropDownItem(name = "Nevada", code = "US_NV"),
            DropDownItem(name = "New Hampshire", code = "US_NH"),
            DropDownItem(name = "New Jersey", code = "US_NJ"),
            DropDownItem(name = "New Mexico", code = "US_NM"),
            DropDownItem(name = "New York", code = "US_NY"),
            DropDownItem(name = "North Carolina", code = "US_NC"),
            DropDownItem(name = "North Dakota", code = "US_ND"),
            DropDownItem(name = "Ohio", code = "US_OH"),
            DropDownItem(name = "Oklahoma", code = "US_OK"),
            DropDownItem(name = "Oregon", code = "US_OR"),
            DropDownItem(name = "Pennsylvania", code = "US_PA"),
            DropDownItem(name = "Rhode Island", code = "US_RI"),
            DropDownItem(name = "South Carolina", code = "US_SC"),
            DropDownItem(name = "South Dakota", code = "US_SD"),
            DropDownItem(name = "Tennessee", code = "US_TN"),
            DropDownItem(name = "Texas", code = "US_TX"),
            DropDownItem(name = "Utah", code = "US_UT"),
            DropDownItem(name = "Vermont", code = "US_VT"),
            DropDownItem(name = "Virginia", code = "US_VA"),
            DropDownItem(name = "Washington", code = "US_WA"),
            DropDownItem(name = "West Virginia", code = "US_WV"),
            DropDownItem(name = "Wisconsin", code = "US_WI"),
            DropDownItem(name = "Wyoming", code = "US_WY"),
        )
}
