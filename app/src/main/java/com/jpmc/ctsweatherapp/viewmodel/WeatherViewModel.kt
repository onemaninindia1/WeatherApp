package com.jpmc.ctsweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmc.ctsweatherapp.api.WeatherHelper
import com.jpmc.ctsweatherapp.models.InputData
import com.jpmc.ctsweatherapp.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepo: WeatherHelper) : ViewModel(){
    private val _uiState = MutableStateFlow<UIState>(UIState.Inactive)
    private val searchQueryOrGeolocation = MutableStateFlow(InputData())
    val query: StateFlow<InputData> get() = searchQueryOrGeolocation

    fun setQuery(queryData: InputData) {
        searchQueryOrGeolocation.value = queryData
    }
    val uiState : StateFlow<UIState>
        get() = _uiState

    fun getWeatherForecast(inputQuery: InputData) = viewModelScope.launch {
        weatherRepo.getWeatherDetails(inputQuery)
            // If Api call is failed, set the State to Error
            .catch {
                _uiState.value = UIState.Error(it.message)
            }
            // If Api call is succeeded, set the State to Success
            // and set the response data to data received from api
            .collect {
                _uiState.value = UIState.ResponseData(it)
            }
    }

    /*private fun weatherDetailsResponse(weatherDetails: WeatherDetails): WeatherDetails {
        //Log.d("Live data:", weatherDetails.toString())
        return WeatherDetails(
            coord = weatherDetails.coord,
            weather = weatherDetails.weather,
            base = weatherDetails.base,
            main = weatherDetails.main,
            visibility = weatherDetails.visibility,
            wind = weatherDetails.wind,
            clouds = weatherDetails.clouds,
            dt = weatherDetails.dt,
            sys = weatherDetails.sys,
            timeZone = weatherDetails.timeZone,
            id = weatherDetails.id,
            name = weatherDetails.name,
            cod = weatherDetails.cod
        )
    }*/
}