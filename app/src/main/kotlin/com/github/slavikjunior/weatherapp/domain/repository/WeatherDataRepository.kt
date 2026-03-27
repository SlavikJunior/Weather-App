package com.github.slavikjunior.weatherapp.domain.repository

import com.github.slavikjunior.weatherapp.domain.model.LocationData
import com.github.slavikjunior.weatherapp.domain.model.WeatherResult

internal interface WeatherDataRepository {

    suspend fun getCurrentWeather(city: String): WeatherResult

    suspend fun getLocationByCity(city: String): LocationData
}
