package com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail

import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.github.slavikjunior.weatherapp.presentation.screen.WeatherScreenDefaults
import com.google.firebase.analytics.FirebaseAnalytics
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val SCREEN_NAME = "WeatherDetail"
private const val SCREEN_CLASS = "WeatherDetailScreen"

@Composable
fun WeatherDetail(
    paddingValues: PaddingValues = PaddingValues(),
    city: String,
    onBack: () -> Unit = {}
) = InnerWeatherDetail(paddingValues = paddingValues, city = city, onBack = onBack)

@Composable
internal fun InnerWeatherDetail(
    paddingValues: PaddingValues = PaddingValues(),
    city: String,
    onBack: () -> Unit = {},
    viewModel: WeatherDetailViewModel = hiltViewModel<WeatherDetailViewModel, WeatherDetailViewModel.Factory>(
        key = city
    ) { factory -> factory.create(city) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        FirebaseAnalytics.getInstance(context).logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, SCREEN_NAME)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, SCREEN_CLASS)
            }
        )
    }

    val gradient = Brush.verticalGradient(listOf(GradientTop, GradientBottom))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(paddingValues)
    ) {
        when (val state = uiState) {
            is WeatherDetailUiState.LoadingState -> LoadingContent()
            is WeatherDetailUiState.ErrorState -> ErrorContent(
                message = state.cause.message ?: stringResource(R.string.error_unknown),
                onRetry = { viewModel.reduce(WeatherDetailEvent.RetryEvent) }
            )
            is WeatherDetailUiState.DefaultState -> DetailContent(
                city = state.city,
                weatherData = state.weatherData,
                onBack = onBack
            )
        }
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
private fun DetailContent(
    city: String,
    weatherData: WeatherData?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(text = stringResource(R.string.action_back), fontSize = 16.sp, color = TextOnGradient)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = city,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = TextOnGradient
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (weatherData != null) {
            WeatherDetailDisplay(weatherData = weatherData)
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TextOnGradient, strokeWidth = 3.dp)
            }
        }
    }
}

@Composable
private fun WeatherDetailDisplay(weatherData: WeatherData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(WeatherScreenDefaults.ICON_URL_FORMAT.format(weatherData.icon))
                .crossfade(true)
                .build(),
            contentDescription = weatherData.description,
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "${weatherData.temp}°C",
            fontSize = 80.sp,
            fontWeight = FontWeight.Thin,
            color = TextOnGradient
        )

        Text(
            text = weatherData.description.replaceFirstChar { it.uppercase() },
            fontSize = 20.sp,
            color = TextSecondaryOnGradient
        )

        Spacer(modifier = Modifier.height(32.dp))

        DetailRow(label = stringResource(R.string.stat_feels_like), value = "${weatherData.feelsLike}°C")
        Spacer(modifier = Modifier.height(12.dp))
        DetailRow(label = stringResource(R.string.stat_humidity), value = "${weatherData.humidity}%")
        Spacer(modifier = Modifier.height(12.dp))
        DetailRow(label = stringResource(R.string.stat_wind_speed), value = "${weatherData.windSpeed} м/с")
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SunCard(label = stringResource(R.string.stat_sunrise), value = formatUnixTime(weatherData.sunrise))
            SunCard(label = stringResource(R.string.stat_sunset), value = formatUnixTime(weatherData.sunSet))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp, color = TextSecondaryOnGradient)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextOnGradient)
    }
}

@Composable
private fun SunCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = TextOnGradient)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 13.sp, color = TextSecondaryOnGradient)
    }
}

private fun formatUnixTime(unixSeconds: Long): String {
    val date = Date(unixSeconds * 1000)
    return SimpleDateFormat(WeatherScreenDefaults.TIME_FORMAT, Locale.getDefault()).format(date)
}