package com.week5.weatherappweek5.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.week5.weatherappweek5.data.model.WeatherCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_cache WHERE city = :city LIMIT 1")
    fun getWeatherByCity(city: String): Flow<WeatherCacheEntity?>

    @Query("SELECT * FROM weather_cache WHERE city = :city LIMIT 1")
    suspend fun getWeatherByCityOnce(city: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeather(entity: WeatherCacheEntity)

    @Query("DELETE FROM weather_cache")
    suspend fun clearAll()
}