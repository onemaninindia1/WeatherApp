package com.jpmc.ctsweatherapp.api

import com.jpmc.ctsweatherapp.models.WeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double? = null,
        @Query("lon") longitude: Double? = null,
        @Query("zip") zipCode: String? = null,
        @Query("q") cityName: String? = null
    ): WeatherDetails
}