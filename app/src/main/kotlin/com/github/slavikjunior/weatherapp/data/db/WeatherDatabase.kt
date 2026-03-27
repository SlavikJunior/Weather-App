package com.github.slavikjunior.weatherapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.slavikjunior.weatherapp.data.db.WeatherDatabase.Companion.WEATHER_DATABASE_VERSION

@Database(entities = [WeatherCacheEntity::class], version = WEATHER_DATABASE_VERSION)
internal abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherCacheDao(): WeatherCacheDao

    private companion object {
        const val WEATHER_DATABASE_VERSION = 1
    }
}
