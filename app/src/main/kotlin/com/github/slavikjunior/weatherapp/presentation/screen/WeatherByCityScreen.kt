package com.github.slavikjunior.weatherapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.github.SlavikJunior.weatherapp.R
import com.github.slavikjunior.weatherapp.domain.model.WeatherData
import com.github.slavikjunior.weatherapp.presentation.ui.theme.CardBackground
import com.github.slavikjunior.weatherapp.presentation.ui.theme.CardBorder
import com.github.slavikjunior.weatherapp.presentation.ui.theme.ErrorRed
import com.github.slavikjunior.weatherapp.presentation.ui.theme.GradientBottom
import com.github.slavikjunior.weatherapp.presentation.ui.theme.GradientTop
import com.github.slavikjunior.weatherapp.presentation.ui.theme.TextOnGradient
import com.github.slavikjunior.weatherapp.presentation.ui.theme.TextSecondaryOnGradient
import com.github.slavikjunior.weatherapp.presentation.viewmodel.WeatherByCityViewModel
import com.github.slavikjunior.weatherapp.presentation.viewmodel.event.WeatherByCityEvent
import com.github.slavikjunior.weatherapp.presentation.viewmodel.state.WeatherByCityUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ICON_URL_FORMAT = "https://openweathermap.org/img/wn/%s@4x.png"
private const val TIME_FORMAT = "HH:mm"

@Composable
fun WeatherByCity(
    paddingValues: PaddingValues = PaddingValues()
) = InnerWeatherByCity(paddingValues = paddingValues)

@Composable
internal fun InnerWeatherByCity(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: WeatherByCityViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { resId ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(resId),
                duration = SnackbarDuration.Short
            )
        }
    }

    val gradient = Brush.verticalGradient(listOf(GradientTop, GradientBottom))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(paddingValues)
    ) {
        when (val state = uiState) {
            is WeatherByCityUiState.LoadingState -> LoadingContent()

            is WeatherByCityUiState.ErrorState -> ErrorContent(
                message = state.cause.message ?: stringResource(R.string.error_unknown),
                onRetry = { viewModel.reduce(WeatherByCityEvent.DismissErrorEvent) }
            )

            is WeatherByCityUiState.DefaultState -> WeatherContent(
                city = state.city,
                weatherData = state.weatherData,
                onCityChange = { viewModel.reduce(WeatherByCityEvent.UpdateCurrentCityEvent(it)) },
                onSearch = { viewModel.reduce(WeatherByCityEvent.GetCurrentWeatherEvent) }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = CardBackground,
                    contentColor = TextOnGradient
                )
            }
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = TextOnGradient, strokeWidth = 3.dp)
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ErrorRed
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            fontSize = 16.sp,
            color = TextSecondaryOnGradient,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
        ) {
            Text(text = stringResource(R.string.error_back_button), color = TextOnGradient)
        }
    }
}

@Composable
private fun WeatherContent(
    city: String,
    weatherData: WeatherData?,
    onCityChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weatherData != null) {
            WeatherDisplay(weatherData = weatherData)
        } else {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.empty_state_hint),
                fontSize = 18.sp,
                color = TextSecondaryOnGradient
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
        SearchBar(city = city, onCityChange = onCityChange, onSearch = onSearch)
    }
}

@Composable
private fun WeatherDisplay(weatherData: WeatherData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = weatherData.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            color = TextOnGradient
        )

        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ICON_URL_FORMAT.format(weatherData.icon))
                .crossfade(true)
                .build(),
            contentDescription = weatherData.description,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "${weatherData.temp}°C",
            fontSize = 72.sp,
            fontWeight = FontWeight.Thin,
            color = TextOnGradient
        )

        Text(
            text = weatherData.description.replaceFirstChar { it.uppercase() },
            fontSize = 18.sp,
            color = TextSecondaryOnGradient
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                label = stringResource(R.string.stat_feels_like),
                value = "${weatherData.feelsLike}°C"
            )
            StatCard(
                label = stringResource(R.string.stat_humidity),
                value = "${weatherData.humidity}%"
            )
            StatCard(
                label = stringResource(R.string.stat_wind),
                value = "${weatherData.windSpeed} м/с"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                label = stringResource(R.string.stat_sunrise),
                value = formatUnixTime(weatherData.sunrise)
            )
            StatCard(
                label = stringResource(R.string.stat_sunset),
                value = formatUnixTime(weatherData.sunSet)
            )
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextOnGradient)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = TextSecondaryOnGradient)
    }
}

@Composable
private fun SearchBar(city: String, onCityChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    color = TextSecondaryOnGradient
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextOnGradient,
                unfocusedTextColor = TextOnGradient,
                focusedBorderColor = TextOnGradient,
                unfocusedBorderColor = CardBorder,
                cursorColor = TextOnGradient
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSearch,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = CardBackground),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = stringResource(R.string.search_button_description),
                fontSize = 14.sp,
                color = TextOnGradient,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatUnixTime(unixSeconds: Long): String {
    val date = Date(unixSeconds * 1000)
    return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(date)
}
