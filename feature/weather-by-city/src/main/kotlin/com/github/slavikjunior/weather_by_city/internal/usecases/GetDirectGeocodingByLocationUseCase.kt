package com.github.slavikjunior.weather_by_city.internal.usecases

import android.util.Log
import com.example.network.api.requests.DirectGeocodingByLocationRequest
import com.example.network.api.responses.DirectGeocodingByLocationResponse
import com.github.slavikjunior.weather_by_city.internal.models.LocationData
import com.github.slavikjunior.weather_by_city.internal.models.RawRequest
import com.github.slavikjunior.weather_by_city.internal.repositoryInterface.WeatherDataRepository

internal class GetDirectGeocodingByLocationUseCase(
    private val repository: WeatherDataRepository
) {

    suspend operator fun invoke(request: RawRequest): LocationData {
        var response: DirectGeocodingByLocationResponse? = null
        try {
            response = repository.directGeocodingByLocation(
                request = DirectGeocodingByLocationRequest(
                    q = request.city
                )
            )
        } catch (cause: Throwable) {
            Log.e(TAG, "Exception while DirectGeocodingByLocationRequest, cause: $cause")
        }

        requireNotNull(response) { "DirectGeocodingByLocationResponse can not be null" }
        return LocationData(
            latitude = response.payload.first().latitude,
            longitude = response.payload.first().longitude
        )
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}