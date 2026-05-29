package com.github.slavikjunior.weatherapp.domain.usecase

import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.model.WeatherResult
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import javax.inject.Inject

internal class GetCurrentWeatherDataByLocationUseCase
@Inject constructor(
    private val repository: WeatherDataRepository
) {

    suspend operator fun invoke(request: RawCity): WeatherResult =
        repository.getCurrentWeather(request.city)
}
