package com.jpmc.ctsweatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jpmc.ctsweatherapp.R
import com.jpmc.ctsweatherapp.databinding.FragmentWeatherBinding
import com.jpmc.ctsweatherapp.di.AppModule
import com.jpmc.ctsweatherapp.models.InputData
import com.jpmc.ctsweatherapp.models.WeatherDetails
import com.jpmc.ctsweatherapp.util.*
import com.jpmc.ctsweatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        binding = FragmentWeatherBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSearchView()
        initWeatherForecast()
    }

    private fun initWeatherForecast() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                weatherViewModel.query.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        weatherViewModel.getWeatherForecast(it)
                    }
                })
                weatherViewModel.res.observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            //binding.weatherLayout.cityName = it.data.
                            it.data.let { res ->
                                //Log.d("Live data:", res.toString())
                                val weatherData = res as WeatherDetails
                                renderUI(weatherData)
                            }
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(
                                binding.rootView,
                                "Something went wrong",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                })
            }
        }
    }

    private fun handleSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val weatherApi = AppModule.provideWeatherAPI(AppModule.provideRetrofit())
                val searchText = query.intOrString()
                when (searchText::class.simpleName) {
                    "String" -> {
                        /*lifecycleScope.launch {
                            val result = weatherApi.getCurrentWeather(cityName = query)
                            Log.d("testing before implementing UI: ", result.toString())
                        }*/
                        val inputData = InputData(null, null, null, query)
                        weatherViewModel.setQuery(inputData)
                    }
                    "Int" -> {
                        if (query.length == 5) {
                            /*lifecycleScope.launch {
                                val result = weatherApi.getCurrentWeather(zipCode = query)
                                Log.d("testing before implementing UI: ", result.toString())
                            }*/
                            val inputData = InputData(null, null, query, null)
                            weatherViewModel.setQuery(inputData)
                        } else {
                            Toast.makeText(activity, R.string.searchbar_hint, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else -> {
                        Toast.makeText(activity, R.string.searchbar_hint, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun renderUI(weatherData: WeatherDetails) {
        binding.weatherLayout.countryCityName.text = getString(
            R.string.tv_country_city_name,
            weatherData.name,
            weatherData.sys.country
        )
        binding.weatherLayout.updatedDate.text = getString(
            R.string.tv_updated_on,
            weatherData.dt.dateConversion()
        )
        binding.weatherLayout.tvTemperature.text =
            weatherData.main.temp.kelvinToCelsius().toString()
        binding.weatherLayout.conditionDescription.text =
            weatherData.weather[0].description
        val imgPath = "${BASE_URL}img/w/${weatherData.weather[0].icon}.png"
        Glide.with(this@WeatherFragment).load(imgPath).into(binding.weatherLayout.ivWeatherCondition)
        binding.weatherLayout.maxTemp.text =
            getString(R.string.tv_temp, weatherData.main.tempMax.kelvinToCelsius().toString())
        binding.weatherLayout.minTemp.text =
            getString(R.string.tv_temp, weatherData.main.tempMin.kelvinToCelsius().toString())
        binding.weatherLayout.weatherDetailsLayout.tvHumidity.text =
            getUnits(weatherData.main.humidity, "%")
        binding.weatherLayout.weatherDetailsLayout.tvPressure.text =
            getUnits(weatherData.main.pressure, "mBar")
        binding.weatherLayout.weatherDetailsLayout.tvVisibility.text =
            convertVisibility(weatherData.visibility)
        binding.weatherLayout.clWeatherLayout.visibility = View.VISIBLE
    }
}