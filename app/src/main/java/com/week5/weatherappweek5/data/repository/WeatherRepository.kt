package com.week5.weatherappweek5.data.repository

import com.week5.weatherappweek5.BuildConfig
import com.week5.weatherappweek5.data.local.WeatherDao
import com.week5.weatherappweek5.data.model.WeatherCacheEntity
import com.week5.weatherappweek5.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val weatherDao: WeatherDao
) {

    fun getWeatherFlow(city: String): Flow<WeatherCacheEntity?> {
        return weatherDao.getWeatherByCity(city.trim())
    }

    suspend fun refreshWeatherIfNeeded(city: String) {
        val trimmedCity = city.trim()
        if (trimmedCity.isEmpty()) return

        val now = System.currentTimeMillis()

        val cached = weatherDao.getWeatherByCityOnce(trimmedCity)

        val cacheValidMillis = 30 * 60 * 1000L // 30 min
        val isCacheValid = cached != null && (now - cached.timestamp) < cacheValidMillis
        if (isCacheValid) return

        val result = RetrofitInstance.weatherApi.getWeatherByCity(
            city = trimmedCity,
            apiKey = BuildConfig.OPENWEATHER_API_KEY
        )

        val entity = WeatherCacheEntity(
            city = trimmedCity,
            temperature = result.main.temp,
            description = result.weather.firstOrNull()?.description ?: "",
            timestamp = now
        )

        weatherDao.upsertWeather(entity)
    }
}