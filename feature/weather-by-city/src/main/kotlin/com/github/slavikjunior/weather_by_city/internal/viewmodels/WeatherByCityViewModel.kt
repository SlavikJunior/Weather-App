package com.github.slavikjunior.weather_by_city.internal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.slavikjunior.weather_by_city.internal.models.RawRequest
import com.github.slavikjunior.weather_by_city.internal.viewmodels.events.WeatherByCityEvent
import com.github.slavikjunior.weather_by_city.internal.usecases.GetCurrentWeatherDataByLocationUseCase
import com.github.slavikjunior.weather_by_city.internal.viewmodels.state.WeatherByCityUiState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WeatherByCityViewModel(
    private val getCurrentWeatherDataByLocation: GetCurrentWeatherDataByLocationUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<WeatherByCityUiState> = MutableStateFlow(WeatherByCityUiState.DefaultState())
    val uiState: StateFlow<WeatherByCityUiState> = _uiState.asStateFlow()

    private val scope = viewModelScope

    internal fun reduce(event: WeatherByCityEvent) {
        when(event) {
            is WeatherByCityEvent.GetCurrentWeatherEvent -> updateCurrentWeather(event)
        }
    }

    private fun updateCurrentWeather(event: WeatherByCityEvent.GetCurrentWeatherEvent) {
        scope.cancel()

        scope.launch {
            getCurrentWeatherDataByLocation(
                request = RawRequest(city = event.city)
            )
        }
    }
}