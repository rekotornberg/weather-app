package com.week5.weatherappweek5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.week5.weatherappweek5.data.local.AppDatabase
import com.week5.weatherappweek5.data.model.WeatherCacheEntity
import com.week5.weatherappweek5.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val city: String = "",
    val isLoading: Boolean = false,
    val cachedWeather: WeatherCacheEntity? = null,
    val errorMessage: String? = null
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = WeatherRepository(db.weatherDao())

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val selectedCity = MutableStateFlow("")

    init {
        viewModelScope.launch {
            selectedCity
                .flatMapLatest { city ->
                    if (city.isBlank()) flowOf(null)
                    else repository.getWeatherFlow(city)
                }
                .collect { entity ->
                    _uiState.update { it.copy(cachedWeather = entity) }
                }
        }
    }

    fun onCityChanged(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
        selectedCity.value = newCity.trim()
    }

    fun fetchWeather() {
        val city = uiState.value.city.trim()
        if (city.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Syötä kaupunki") }
            return
        }

        selectedCity.value = city

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.refreshWeatherIfNeeded(city)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Sään haku epäonnistui"
                    )
                }
            }
        }
    }
}