package com.jpmc.ctsweatherapp.api

import com.jpmc.ctsweatherapp.models.InputData
import com.jpmc.ctsweatherapp.models.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherHelper {
    suspend fun getWeatherDetails(query: InputData): Flow<WeatherDetails>
}