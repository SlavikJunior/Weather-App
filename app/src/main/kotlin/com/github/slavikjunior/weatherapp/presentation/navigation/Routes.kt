package com.github.slavikjunior.weatherapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object WeatherByCityRoute : Route

    @Serializable
    data class WeatherDetailRoute(val city: String) : Route
}