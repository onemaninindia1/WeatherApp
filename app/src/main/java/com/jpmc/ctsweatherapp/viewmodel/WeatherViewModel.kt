package com.jpmc.ctsweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmc.ctsweatherapp.api.WeatherHelper
import com.jpmc.ctsweatherapp.models.InputData
import com.jpmc.ctsweatherapp.models.WeatherDetails
import com.jpmc.ctsweatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepo: WeatherHelper) : ViewModel(){
    private val response = MutableLiveData<Resource>()
    private val searchQueryOrGeolocation = MutableLiveData<InputData>()
    val query: LiveData<InputData> get() = searchQueryOrGeolocation

    fun setQuery(queryData: InputData){
        searchQueryOrGeolocation.value = queryData
    }
    val res : LiveData<Resource>
        get() = response

    fun getWeatherForecast(inputQuery: InputData) = viewModelScope.launch {
        response.postValue(Resource.loading(null))
        weatherRepo.getWeatherDetails(inputQuery)
            // If Api call is failed, set the State to Error
            .catch {
                response.postValue(Resource.error(it.message.toString(), null))
            }
            // If Api call is succeeded, set the State to Success
            // and set the response data to data received from api
            .collect {
                response.value = it.let {
                    Resource.success(it)
                }
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