package com.github.slavikjunior.weatherapp

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived data: ${message.data}")

        val kind = message.data[KIND_KEY] ?: return
        val title = message.data[TITLE_KEY] ?: return
        val body = message.data[MESSAGE_KEY] ?: return

        showNotification(kind = kind, title = title, body = body)
    }

    private fun showNotification(kind: String, title: String, body: String) {
        val channelId = when (kind) {
            KIND_PROMO -> WeatherApplication.CHANNEL_PROMO
            KIND_AUTH -> WeatherApplication.CHANNEL_AUTH
            KIND_WEATHER -> WeatherApplication.CHANNEL_WEATHER
            else -> WeatherApplication.CHANNEL_DEFAULT
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val priority = if (kind == KIND_AUTH) NotificationCompat.PRIORITY_HIGH
        else NotificationCompat.PRIORITY_DEFAULT

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        if (hasPermission) {
            NotificationManagerCompat.from(this)
                .notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    private companion object {
        const val KIND_KEY = "kind"
        const val TITLE_KEY = "title"
        const val MESSAGE_KEY = "message"
        const val TAG = "MessagingService"
        const val KIND_PROMO = "promo"
        const val KIND_AUTH = "auth"
        const val KIND_WEATHER = "weather"
    }
}
