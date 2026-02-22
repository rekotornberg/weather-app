package com.week5.weatherappweek5.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(

    @PrimaryKey
    val city: String,

    val temperature: Double,

    val description: String,

    val timestamp: Long
)