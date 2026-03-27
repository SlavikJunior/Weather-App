package com.github.slavikjunior.weatherapp.data.network.service

import com.github.slavikjunior.weatherapp.data.network.response.CurrentWeatherResponse
import com.github.slavikjunior.weatherapp.data.network.response.DirectGeocodingByLocationResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface OpenWeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(@QueryMap currentWeatherRequest: Map<String, String>): CurrentWeatherResponse?

    @GET("geo/1.0/direct")
    suspend fun directGeocoding(@QueryMap directGeocodingByLocationRequest: Map<String, String>): List<DirectGeocodingByLocationResponse>?
}
