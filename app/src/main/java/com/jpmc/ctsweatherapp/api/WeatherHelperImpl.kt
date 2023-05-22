package com.jpmc.ctsweatherapp.api

import com.jpmc.ctsweatherapp.models.InputData
import com.jpmc.ctsweatherapp.models.WeatherDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherHelperImpl @Inject constructor(private val getWeather: WeatherAPI): WeatherHelper {
    override suspend fun getWeatherDetails(query: InputData): Flow<WeatherDetails> = flow {
        emit(getWeather.getCurrentWeather(query.latitude, query.longitude, query.zipCode, query.cityName))
    }
}