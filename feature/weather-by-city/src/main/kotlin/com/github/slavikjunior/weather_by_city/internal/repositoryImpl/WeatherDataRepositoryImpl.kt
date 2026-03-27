package com.github.slavikjunior.weather_by_city.internal.repositoryImpl

import android.util.Log
import com.example.network.api.requests.CurrentWeatherRequest
import com.example.network.api.requests.DirectGeocodingByLocationRequest
import com.example.network.api.responses.CurrentWeatherResponse
import com.example.network.api.responses.DirectGeocodingByLocationResponse
import com.github.slavikjunior.weather_by_city.api.service.OpenWeatherApiService
import com.github.slavikjunior.utils.api.toQueryMap
import com.github.slavikjunior.weather_by_city.internal.repositoryInterface.WeatherDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class WeatherDataRepositoryImpl(
    private val apiService: OpenWeatherApiService,
    private val dispatcher: CoroutineDispatcher
) : WeatherDataRepository {
    override suspend fun getCurrentWeather(request: CurrentWeatherRequest): CurrentWeatherResponse {
        var response: CurrentWeatherResponse? = null
        try {
            withContext(dispatcher) {
                response = apiService.getCurrentWeatherData(
                    currentWeatherRequest = CurrentWeatherRequest.toQueryMap()
                )
            }
        } catch (cause: Throwable) {
            Log.e(TAG, "Error in WeatherDataRepositoryImpl in method getCurrentWeather, cause: $cause")
        }

        response?.let { return it }
        throw Throwable("CurrentWeatherResponse in WeatherDataRepositoryImpl is null")
    }

    override suspend fun directGeocodingByLocation(request: DirectGeocodingByLocationRequest): DirectGeocodingByLocationResponse {
        var response: DirectGeocodingByLocationResponse? = null
        try {
            withContext(dispatcher) {
                response = apiService.directGeocoding(
                    directGeocodingByLocationRequest = request.toQueryMap()
                )
            }
        } catch (cause: Throwable) {
            Log.e(TAG, "Error in WeatherDataRepositoryImpl in method directGeocodingByLocation, cause: $cause")
        }

        response?.let { return it }
        throw Throwable("DirectGeocodingByLocationResponse in WeatherDataRepositoryImpl is null")
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}