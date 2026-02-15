package com.week5.weatherappweek5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.week5.weatherappweek5.BuildConfig
import com.week5.weatherappweek5.data.model.WeatherResponse
import com.week5.weatherappweek5.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val city: String = "",
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val errorMessage: String? = null
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun onCityChanged(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
    }

    fun fetchWeather() {
        val city = uiState.value.city.trim()

        if (city.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Syötä kaupunki") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, weather = null) }

            try {
                val result = RetrofitInstance.weatherApi.getWeatherByCity(
                    city = city,
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )
                _uiState.update { it.copy(isLoading = false, weather = result) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Sään haku epäonnistui: tarkista kaupungin nimi!"
                    )
                }
            }
        }
    }
}
