package com.github.slavikjunior.weatherapp.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherRequest(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("units") val units: String = "metric",
    @SerialName("lang") val lang: String = "ru"
)
