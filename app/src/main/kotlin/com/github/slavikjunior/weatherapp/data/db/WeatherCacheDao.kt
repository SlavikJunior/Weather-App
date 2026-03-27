package com.github.slavikjunior.weatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface WeatherCacheDao {

    @Query("SELECT * FROM weather_cache WHERE city = :city")
    suspend fun getByCity(city: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WeatherCacheEntity)
}
