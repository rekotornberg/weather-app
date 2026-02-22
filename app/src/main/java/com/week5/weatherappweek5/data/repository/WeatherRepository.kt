package com.week5.weatherappweek5.data.repository

import com.week5.weatherappweek5.BuildConfig
import com.week5.weatherappweek5.data.local.WeatherDao
import com.week5.weatherappweek5.data.model.WeatherCacheEntity
import com.week5.weatherappweek5.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val weatherDao: WeatherDao
) {

    // UI voi kuunnella tietyn kaupungin tallennettua dataa
    fun getWeatherFlow(city: String): Flow<WeatherCacheEntity?> {
        return weatherDao.getWeatherByCity(city.trim())
    }

    // Hakee API:sta vain jos kaupungin cache puuttuu tai on vanhempi kuin 30 min
    suspend fun refreshWeatherIfNeeded(city: String) {
        val trimmedCity = city.trim()
        if (trimmedCity.isEmpty()) return

        val now = System.currentTimeMillis()

        // Tarkistetaan cache TÄMÄN kaupungin mukaan
        val cached = weatherDao.getWeatherByCityOnce(trimmedCity)

        val cacheValidMillis = 30 * 60 * 1000L // 30 min
        val isCacheValid = cached != null && (now - cached.timestamp) < cacheValidMillis
        if (isCacheValid) return

        // Cache puuttuu tai on vanha -> hae API:sta
        val result = RetrofitInstance.weatherApi.getWeatherByCity(
            city = trimmedCity,
            apiKey = BuildConfig.OPENWEATHER_API_KEY
        )

        val entity = WeatherCacheEntity(
            city = trimmedCity, // pidetään käyttäjän syöte avaimena
            temperature = result.main.temp,
            description = result.weather.firstOrNull()?.description ?: "",
            timestamp = now
        )

        weatherDao.upsertWeather(entity)
    }
}