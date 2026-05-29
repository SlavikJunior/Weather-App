package com.github.slavikjunior.weatherapp.data.repository

import android.util.Log
import com.github.slavikjunior.weatherapp.data.db.WeatherCacheDao
import com.github.slavikjunior.weatherapp.data.db.toCacheEntity
import com.github.slavikjunior.weatherapp.data.db.toWeatherData
import com.github.slavikjunior.weatherapp.data.network.request.CurrentWeatherRequest
import com.github.slavikjunior.weatherapp.data.network.request.DirectGeocodingByLocationRequest
import com.github.slavikjunior.weatherapp.data.network.response.CurrentWeatherResponse
import com.github.slavikjunior.weatherapp.data.network.service.OpenWeatherApiService
import com.github.slavikjunior.weatherapp.data.mapper.toDomainModel
import com.github.slavikjunior.weatherapp.data.utils.toQueryMap
import com.github.slavikjunior.weatherapp.domain.model.DataSource
import com.github.slavikjunior.weatherapp.domain.model.LocationData
import com.github.slavikjunior.weatherapp.domain.model.WeatherResult
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class WeatherDataRepositoryImpl
@Inject constructor(
    private val apiService: OpenWeatherApiService,
    private val dispatcher: CoroutineDispatcher,
    private val cacheDao: WeatherCacheDao
) : WeatherDataRepository {

    override suspend fun getCurrentWeather(city: String): WeatherResult {
        val cacheKey = city.lowercase().trim()
        val cached = cacheDao.getByCity(cacheKey)
        val nowSeconds = System.currentTimeMillis() / 1000

        if (cached != null && (nowSeconds - cached.cachedAt) < CACHE_TTL_SECONDS) {
            return WeatherResult(data = cached.toWeatherData(), source = DataSource.CACHE)
        }

        val locationData = getLocationByCity(city)
        val response = fetchCurrentWeather(
            CurrentWeatherRequest(
                latitude = locationData.latitude,
                longitude = locationData.longitude
            )
        )
        val weatherData = response.toDomainModel()
        cacheDao.insert(weatherData.toCacheEntity(city = cacheKey, cachedAt = nowSeconds))

        return WeatherResult(data = weatherData, source = DataSource.SERVER)
    }

    override suspend fun getLocationByCity(city: String): LocationData {
        val response = try {
            withContext(dispatcher) {
                apiService.directGeocoding(
                    directGeocodingByLocationRequest = DirectGeocodingByLocationRequest(q = city)
                        .toQueryMap()
                )
            }
        } catch (cause: Throwable) {
            Log.e(TAG, "Error in getLocationByCity: $cause")
            throw cause
        }
        val first = response?.firstOrNull()
            ?: throw IllegalStateException(CITY_NOT_FOUND_MESSAGE.format(city))
        return LocationData(latitude = first.latitude, longitude = first.longitude)
    }

    private suspend fun fetchCurrentWeather(request: CurrentWeatherRequest): CurrentWeatherResponse {
        return try {
            withContext(dispatcher) {
                apiService.getCurrentWeatherData(currentWeatherRequest = request.toQueryMap())
            } ?: throw IllegalStateException("CurrentWeatherResponse is null")
        } catch (cause: Throwable) {
            Log.e(TAG, "Error in fetchCurrentWeather: $cause")
            throw cause
        }
    }

    private companion object {
        const val TAG = "WeatherRepository"
        const val CACHE_TTL_SECONDS = 60L
        const val CITY_NOT_FOUND_MESSAGE = "Город '%s' не найден"
    }
}
