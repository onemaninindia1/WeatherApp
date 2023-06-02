package com.jpmc.ctsweatherapp.util

import com.jpmc.ctsweatherapp.models.WeatherDetails

sealed class UIState {
    object Inactive : UIState()
    object Loading : UIState()
    data class ResponseData(val data: WeatherDetails) : UIState()
    data class Error(val error: String?) : UIState()
}
