package com.github.slavikjunior.weather_by_city.api.service

import com.example.network.api.responses.CurrentWeatherResponse
import com.example.network.api.responses.DirectGeocodingByLocationResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface
OpenWeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(@QueryMap currentWeatherRequest: Map<String, String>): CurrentWeatherResponse?

    @GET("geo/1.0/direct")
    suspend fun directGeocoding(@QueryMap directGeocodingByLocationRequest: Map<String, String>): DirectGeocodingByLocationResponse?
}