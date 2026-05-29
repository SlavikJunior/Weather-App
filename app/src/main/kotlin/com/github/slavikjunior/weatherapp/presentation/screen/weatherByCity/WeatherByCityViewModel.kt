package com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.SlavikJunior.weatherapp.R
import com.github.slavikjunior.weatherapp.domain.model.DataSource
import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.usecase.GetCurrentWeatherDataByLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherByCityViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCurrentWeatherDataByLocation: GetCurrentWeatherDataByLocationUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<WeatherByCityUiState> =
        MutableStateFlow(WeatherByCityUiState.DefaultState())
    val uiState: StateFlow<WeatherByCityUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val snackbarMessage: SharedFlow<Int> = _snackbarMessage.asSharedFlow()

    private var currentJob: Job? = null

    init {
        val savedCity = savedStateHandle.get<String>(KEY_CITY)
        if (!savedCity.isNullOrBlank()) {
            _uiState.update { WeatherByCityUiState.DefaultState(city = savedCity) }
            updateCurrentWeather(city = savedCity)
        }
    }

    internal fun reduce(event: WeatherByCityEvent) {
        when (event) {
            is WeatherByCityEvent.GetCurrentWeatherEvent -> {
                val city = (_uiState.value as? WeatherByCityUiState.DefaultState)?.city ?: return
                updateCurrentWeather(city = city.trim())
            }
            is WeatherByCityEvent.UpdateCurrentCityEvent -> updateCurrentCity(event)
            is WeatherByCityEvent.DismissErrorEvent -> dismissError()
        }
    }

    private fun updateCurrentCity(event: WeatherByCityEvent.UpdateCurrentCityEvent) {
        _uiState.update { currentState ->
            when (currentState) {
                is WeatherByCityUiState.DefaultState -> currentState.copy(city = event.city)
                is WeatherByCityUiState.ErrorState -> WeatherByCityUiState.DefaultState(city = event.city)
                is WeatherByCityUiState.LoadingState -> currentState
            }
        }
    }

    private fun dismissError() {
        val errorState = _uiState.value as? WeatherByCityUiState.ErrorState ?: return
        _uiState.update { WeatherByCityUiState.DefaultState(city = errorState.city) }
    }

    private fun updateCurrentWeather(city: String) {
        if (city.isBlank()) return

        currentJob?.cancel()
        _uiState.update { WeatherByCityUiState.LoadingState(requestedCity = city) }
        savedStateHandle[KEY_CITY] = city

        currentJob = viewModelScope.launch {
            try {
                val result = getCurrentWeatherDataByLocation(request = RawCity(city = city))
                _uiState.update {
                    WeatherByCityUiState.DefaultState(city = city, weatherData = result.data)
                }
                val messageRes = when (result.source) {
                    DataSource.SERVER -> R.string.data_source_server
                    DataSource.CACHE -> R.string.data_source_cache
                }
                _snackbarMessage.emit(messageRes)
            } catch (cause: Throwable) {
                _uiState.update {
                    WeatherByCityUiState.ErrorState(message = cause.message ?: "", city = city)
                }
            }
        }
    }

    private companion object {
        const val KEY_CITY = "city"
    }
}