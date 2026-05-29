package com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.usecase.GetCurrentWeatherDataByLocationUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WeatherDetailViewModel.Factory::class)
internal class WeatherDetailViewModel @AssistedInject constructor(
    @Assisted val city: String,
    private val getCurrentWeatherDataByLocation: GetCurrentWeatherDataByLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherDetailUiState>(WeatherDetailUiState.LoadingState(city))
    val uiState = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(city: String): WeatherDetailViewModel
    }

    init {
        loadWeather()
    }

    internal fun reduce(event: WeatherDetailEvent) {
        when (event) {
            is WeatherDetailEvent.RetryEvent -> loadWeather()
            is WeatherDetailEvent.DismissErrorEvent -> dismissError()
        }
    }

    private fun dismissError() {
        val errorState = _uiState.value as? WeatherDetailUiState.ErrorState ?: return
        _uiState.update { WeatherDetailUiState.DefaultState(city = errorState.city) }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { WeatherDetailUiState.LoadingState(requestedCity = city) }
            try {
                val result = getCurrentWeatherDataByLocation(RawCity(city = city))
                _uiState.update { WeatherDetailUiState.DefaultState(city = city, weatherData = result.data) }
            } catch (cause: Throwable) {
                _uiState.update {
                    WeatherDetailUiState.ErrorState(message = cause.message ?: "", city = city)
                }
            }
        }
    }
}