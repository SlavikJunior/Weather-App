package com.github.slavikjunior.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.github.SlavikJunior.weatherapp.BuildConfig
import com.github.slavikjunior.weatherapp.data.db.WeatherCacheDao
import com.github.slavikjunior.weatherapp.data.db.WeatherDatabase
import com.github.slavikjunior.weatherapp.data.network.interceptor.OpenWeatherApiKeyInterceptor
import com.github.slavikjunior.weatherapp.data.network.service.OpenWeatherApiService
import com.github.slavikjunior.weatherapp.data.repository.WeatherDataRepositoryImpl
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.openweathermap.org/"
    private const val DATABASE_NAME = "weather_cache.db"
    private const val CONTENT_TYPE = "application/json"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(OpenWeatherApiKeyInterceptor(BuildConfig.OPEN_WEATHER_API_KEY))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideOpenWeatherApiService(retrofit: Retrofit): OpenWeatherApiService =
        retrofit.create(OpenWeatherApiService::class.java)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    internal fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME).build()

    @Provides
    internal fun provideWeatherCacheDao(database: WeatherDatabase): WeatherCacheDao =
        database.weatherCacheDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    internal abstract fun bindWeatherDataRepository(impl: WeatherDataRepositoryImpl): WeatherDataRepository
}
