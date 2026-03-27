package com.github.slavikjunior.weatherapp.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectGeocodingByLocationResponse(
    @SerialName("name") val name: String,
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("country") val country: String? = null,
    @SerialName("state") val state: String? = null
)
