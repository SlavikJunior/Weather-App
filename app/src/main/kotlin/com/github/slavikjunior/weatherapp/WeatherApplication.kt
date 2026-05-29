package com.github.slavikjunior.weatherapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID
import androidx.core.content.edit

@HiltAndroidApp
class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initCrashlyticsUser()
        createNotificationChannels()
    }

    private fun initCrashlyticsUser() {
        val prefs = getSharedPreferences(APP_PREFS_NAME, MODE_PRIVATE)
        val userId = prefs.getString(KEY_USER_ID, null) ?: UUID.randomUUID().toString().also { newId ->
            prefs.edit { putString(KEY_USER_ID, newId) }
        }
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    private fun createNotificationChannels() {
        val manager = getSystemService(NotificationManager::class.java)
        listOf(
            NotificationChannel(CHANNEL_PROMO, CHANNEL_PROMO_NAME, NotificationManager.IMPORTANCE_DEFAULT),
            NotificationChannel(CHANNEL_AUTH, CHANNEL_AUTH_NAME, NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_WEATHER, CHANNEL_WEATHER_NAME, NotificationManager.IMPORTANCE_DEFAULT),
            NotificationChannel(CHANNEL_DEFAULT, CHANNEL_DEFAULT_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        ).forEach { manager.createNotificationChannel(it) }
    }

    companion object {
        const val APP_PREFS_NAME = "weather_app_prefs"
        const val KEY_USER_ID = "user_id"
        const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
        const val CHANNEL_PROMO = "channel_promo"
        const val CHANNEL_PROMO_NAME = "Промо-акции"
        const val CHANNEL_AUTH = "channel_auth"
        const val CHANNEL_AUTH_NAME = "Безопасность"
        const val CHANNEL_WEATHER = "channel_weather"
        const val CHANNEL_WEATHER_NAME = "Погода"
        const val CHANNEL_DEFAULT = "channel_default"
        const val CHANNEL_DEFAULT_NAME = "Общие"
    }
}
