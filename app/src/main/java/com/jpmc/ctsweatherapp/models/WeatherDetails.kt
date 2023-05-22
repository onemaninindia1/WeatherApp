package com.jpmc.ctsweatherapp.models

import com.google.gson.annotations.SerializedName

data class WeatherDetails(
    @SerializedName("coord")
    val coord: Coord = Coord(),
    @SerializedName("weather")
    val weather: List<Weather> = listOf(),
    @SerializedName("base")
    val base: String = "",
    @SerializedName("main")
    val main: Main = Main(),
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("wind")
    val wind: Wind = Wind(),
    @SerializedName("clouds")
    val clouds: Clouds = Clouds(),
    @SerializedName("dt")
    val dt: Int = 0,
    @SerializedName("sys")
    val sys: Sys = Sys(),
    @SerializedName("timezone")
    val timeZone: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("cod")
    val cod: Int = 0
)
