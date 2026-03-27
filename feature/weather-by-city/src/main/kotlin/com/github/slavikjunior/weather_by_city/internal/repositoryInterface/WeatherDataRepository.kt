package com.github.slavikjunior.weather_by_city.internal.repositoryInterface

import com.example.network.api.requests.CurrentWeatherRequest
import com.example.network.api.requests.DirectGeocodingByLocationRequest
import com.example.network.api.responses.CurrentWeatherResponse
import com.example.network.api.responses.DirectGeocodingByLocationResponse

interface WeatherDataRepository {

    suspend fun getCurrentWeather(request: CurrentWeatherRequest): CurrentWeatherResponse

    suspend fun directGeocodingByLocation(request: DirectGeocodingByLocationRequest): DirectGeocodingByLocationResponse
}