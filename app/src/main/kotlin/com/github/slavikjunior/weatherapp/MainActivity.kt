package com.github.slavikjunior.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.github.slavikjunior.weatherapp.presentation.navigation.Route
import com.github.slavikjunior.weatherapp.presentation.screen.onboarding.OnboardingBottomSheet
import com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity.WeatherByCity
import com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail.WeatherDetail
import com.github.slavikjunior.weatherapp.presentation.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold { paddingValues ->
                    WeatherApp(paddingValues)
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
internal fun WeatherApp(paddingValues: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(WeatherApplication.APP_PREFS_NAME, android.content.Context.MODE_PRIVATE) }
    var showOnboarding by remember { mutableStateOf(!prefs.getBoolean(WeatherApplication.KEY_ONBOARDING_SHOWN, false)) }

    val backStack = rememberNavBackStack(Route.WeatherByCityRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.WeatherByCityRoute> {
                WeatherByCity(
                    paddingValues = paddingValues,
                    onNavigateToDetail = { city -> backStack.add(Route.WeatherDetailRoute(city)) }
                )
            }
            entry<Route.WeatherDetailRoute> { route ->
                WeatherDetail(
                    paddingValues = paddingValues,
                    city = route.city,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )

    if (showOnboarding) {
        OnboardingBottomSheet(
            onDismiss = {
                prefs.edit { putBoolean(WeatherApplication.KEY_ONBOARDING_SHOWN, true) }
                showOnboarding = false
            }
        )
    }
}
