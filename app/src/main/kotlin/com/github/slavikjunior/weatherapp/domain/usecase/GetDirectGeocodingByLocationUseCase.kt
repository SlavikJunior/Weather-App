package com.github.slavikjunior.weatherapp.domain.usecase

import com.github.slavikjunior.weatherapp.domain.model.LocationData
import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import javax.inject.Inject

internal class GetDirectGeocodingByLocationUseCase
@Inject constructor(
    private val repository: WeatherDataRepository
) {

    suspend operator fun invoke(request: RawCity): LocationData =
        repository.getLocationByCity(request.city)
}
