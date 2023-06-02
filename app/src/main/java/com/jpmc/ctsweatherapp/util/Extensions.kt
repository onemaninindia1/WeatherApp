package com.jpmc.ctsweatherapp.util

import com.jpmc.ctsweatherapp.models.Coord
import com.jpmc.ctsweatherapp.models.InputData
import java.text.SimpleDateFormat
import java.util.*

fun Int.dateConversion(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault() // user's default time zone
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this.toString()
}

fun String.intOrString() = toIntOrNull() ?: this

fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}

fun convertVisibility(visibility: Int): String {
    return "${visibility / 1000.0} km"
}

fun getUnits(value: Any, unit: String): String {
    return "$value$unit"
}

fun loadByDefaultLocation(): InputData {
    val lastLocation = Coord()
    return InputData(lastLocation.lat, lastLocation.lon, null, null)
}

/* fun dateConversion(timestamp: Long): String {
        val secondApiFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy'T'HH:mm:ss'Z'")
        val timestampAsDateString = DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(timestamp))
        val date = LocalDate.parse(timestampAsDateString, secondApiFormat).toString()
        return date
}*/