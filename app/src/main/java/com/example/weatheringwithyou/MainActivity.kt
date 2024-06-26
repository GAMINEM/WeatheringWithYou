package com.example.weatheringwithyou

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatheringwithyou.R.drawable.snowimg
import com.example.weatheringwithyou.R.raw.cloud
import com.example.weatheringwithyou.R.raw.rain
import com.example.weatheringwithyou.R.raw.snow
import com.example.weatheringwithyou.R.raw.sun
import com.example.weatheringwithyou.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWeather("Kolkata")
        setupSearchCityListener()
    }

    private fun setupSearchCityListener() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchWeather(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @SuppressLint("WrongViewCast")
    private fun fetchWeather(cityname: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val response = retrofit.getWeather(cityname, "ee5686845c99254b413ea21c38a53dc7", "metric")

        response.enqueue(object : Callback<WeatherReport?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<WeatherReport?>,
                response: Response<WeatherReport?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { weatherReport ->
                        updateUIWithWeatherData(weatherReport, cityname)
                    } ?: run {
                       // Log.d("MainActivity", "Response Body is Null")
                    }
                } else {
                    //Log.d("MainActivity", "Request Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherReport?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun updateUIWithWeatherData(weatherReport: WeatherReport, cityname: String) {
        val maxtemp = weatherReport.main.temp_max.toString()
        val mintemp = weatherReport.main.temp_min.toString()
        val temperature = weatherReport.main.temp.toString()
        val humid = weatherReport.main.humidity.toString()
        val pressure = weatherReport.main.pressure.toString()
        val condition = weatherReport.weather.firstOrNull()?.main ?: "unknown"
        val windSpeed = weatherReport.wind.speed.toString() ?: "N/A"
        val sunrise = weatherReport.sys.sunrise.toLong() ?: "N/A"
        val sunset = weatherReport.sys.sunset.toLong()?: "N/A"

        binding.temp.text = "$temperature °C"
        binding.max.text = "Max: $maxtemp °C"
        binding.min.text = "Min: $mintemp °C"
        binding.humidity.text = "$humid %"
        binding.sealvl.text = "$pressure hPa"
        binding.cond.text = condition
        binding.weather.text = condition
        binding.windspeed.text = "$windSpeed KM/H"
        binding.sunrise.text = getCurrentTime((weatherReport.sys?.sunrise?: 0).toLong())
        binding.sunset.text = getCurrentTime((weatherReport.sys?.sunset?: 0).toLong())
        binding.day.text = getCurrentDayName()
        binding.date.text = getCurrentDate()
        binding.cityname.text = cityname

        updateBackgroundAndAnimation(condition)
    }

    @SuppressLint("ResourceType")
    private fun updateBackgroundAndAnimation(condition: String) {
        when (condition) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottie.setAnimation(sun)
            }
            "Haze", "Mist", "Partly Cloud", "Clouds", "Overcast", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.cloudimg)
                binding.lottie.setAnimation(cloud)
            }
            "Light Rain","Rain","Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain)
                binding.lottie.setAnimation(rain)
            }
            "Light Snow","Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(snowimg)
                binding.lottie.setAnimation(snow)
            }else->{
            binding.root.setBackgroundResource(R.drawable.sunny)
            binding.lottie.setAnimation(sun)
            }
        }
        binding.lottie.playAnimation()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun getCurrentTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun getCurrentDayName(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}
