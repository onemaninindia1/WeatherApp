package com.jpmc.ctsweatherapp.models

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    var lat: Double = 0.0, //39.6123,
    @SerializedName("lon")
    var lon: Double = 0.0 //-104.8799
)
