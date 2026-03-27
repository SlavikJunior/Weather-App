package com.example.network.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectGeocodingByLocationRequest(
    @SerialName("q") val q: String,
    @SerialName("limit") val limit: Int = 1
)
