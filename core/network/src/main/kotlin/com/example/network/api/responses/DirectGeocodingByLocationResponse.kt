package com.example.network.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectGeocodingByLocationResponse(
    val payload: List<DirectGeocodingByLocationResponsePayload>
)

@Serializable
data class DirectGeocodingByLocationResponsePayload(
    @SerialName("name") val name: String,
    @SerialName("local_names") val localNames: Map<String, String>? = null,
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("country") val country: String?,
    @SerialName("state") val state: String? = null
)