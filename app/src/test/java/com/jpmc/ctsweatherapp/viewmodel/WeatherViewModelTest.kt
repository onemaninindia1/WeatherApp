package com.jpmc.ctsweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jpmc.ctsweatherapp.api.WeatherHelper
import com.jpmc.ctsweatherapp.models.*
import com.jpmc.ctsweatherapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class WeatherViewModelTest {

    private lateinit var weatherViewModel: WeatherViewModel
    private val inputData = InputData(39.6123, -104.8799, null, null)
    private val weatherDetails = WeatherDetails(
        coord = Coord(lat = 39.6123, lon = -104.8799),
        weather = listOf(
            Weather(
                id = 721,
                main = "Haze",
                description = "haze",
                icon = "50d"
            )
        ),
        base = "stations",
        main = Main(
            temp = 292.67,
            pressure = 1017.0,
            humidity = 48,
            tempMin = 289.76,
            tempMax = 293.87,
        ),
        visibility = 8047,
        wind = Wind(speed = 2.57, deg = 320.0),
        clouds = Clouds(all = 75),
        dt = 1684624129,
        sys = Sys(
            type = 2,
            id = 2004157,
            country = "US",
            sunrise = 1684582879,
            sunset = 1684635062
        ),
        timeZone = -21600,
        id = 0,
        name = "Englewood",
        cod = 200
    )

    private lateinit var repo: WeatherHelper
    private lateinit var query: MutableLiveData<InputData>
    private lateinit var res : MutableLiveData<Resource>

    @BeforeEach
    fun setUp() {
        repo = mockk(relaxed = true)
        weatherViewModel = WeatherViewModel(repo)
        //query.value = inputData
        //res.value = Resource(Status.SUCCESS, weatherDetails, null)
    }

    @org.junit.jupiter.api.Test
    fun getQuery() {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setQuery with geolocation`() = runTest  {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        try {
            weatherViewModel.setQuery(inputData)
            assertEquals(inputData, weatherViewModel.query.value)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `getWeatherForecast return weather by geolocation`() = runBlocking {
        val weather: Flow<WeatherDetails> = flow {
            emit(weatherDetails)
        }
        coEvery { repo.getWeatherDetails(inputData) } returns weather
        assertEquals(weatherDetails, weather.first())
    }
}