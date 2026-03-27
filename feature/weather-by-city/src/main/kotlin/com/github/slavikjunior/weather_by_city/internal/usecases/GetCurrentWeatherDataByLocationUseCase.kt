package com.github.slavikjunior.weather_by_city.internal.usecases

import android.util.Log
import com.example.network.api.requests.CurrentWeatherRequest
import com.example.network.api.responses.CurrentWeatherResponse
import com.github.slavikjunior.weather_by_city.internal.models.LocationData
import com.github.slavikjunior.weather_by_city.internal.models.RawRequest
import com.github.slavikjunior.weather_by_city.internal.models.WeatherData
import com.github.slavikjunior.weather_by_city.internal.repositoryInterface.WeatherDataRepository

internal class GetCurrentWeatherDataByLocationUseCase(
    private val repository: WeatherDataRepository,
    /* Подумал, что в этом случае горизонтальная зависимость не будет злом */
    private val getDirectGeocodingByLocation: GetDirectGeocodingByLocationUseCase
) {

    suspend operator fun invoke(request: RawRequest): WeatherData {
        var locationData: LocationData? = null
        try {
            locationData = getDirectGeocodingByLocation.invoke(request = request)
        } catch (cause: Throwable) {
            Log.e(TAG, "Transitive exception in GetCurrentWeatherDataByLocationUseCase, from GetDirectGeocodingByLocationUseCase")
        }

        requireNotNull(locationData) { "Location data can not be null" }
        var response: CurrentWeatherResponse? = null
        try {
            response = repository.getCurrentWeather(
                request = CurrentWeatherRequest(
                    latitude = locationData.latitude,
                    longitude = locationData.longitude
                )
            )
        } catch (cause: Throwable) {
            Log.e(TAG, "Exception while CurrentWeatherRequest, cause: $cause")
        }

        requireNotNull(response) { "CurrentWeatherResponse can not be null" }
        return with(response) {
            WeatherData(
                name = response.name,
                description = response.weather.description,
                icon = weather.icon,
                temp = main.temp.toInt(),
                feelsLike = main.feelsLike.toInt(),
                windSpeed = wind.speed,
                sunrise = system.sunrise,
                sunSet = system.sunset
            )
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}