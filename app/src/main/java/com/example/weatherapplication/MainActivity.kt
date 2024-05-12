package com.example.weatherapplication
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.google.android.material.color.utilities.ViewingConditions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private  val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchweatherData("Faridabad")
        Searchcity()
    }

    private fun Searchcity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchweatherData(query)
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun fetchweatherData(city:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(WeatherAPI::class.java)
        val response = retrofit.getWeather(city, "fcdce9b4813a67587c1ef446ca68bd5c", "metric")
        response.enqueue(object :Callback<WeatherApp>{
                override fun onResponse(
                    call: Call<WeatherApp>, response: Response<WeatherApp>) {
                    val responseBody=response.body()
                    if(response.isSuccessful&&responseBody !=null)
                    {
                        val temperature =responseBody.main.temp.toString()
                        val humidity=responseBody.main.humidity
                        val windspeed=responseBody.wind
                        val sunrise=responseBody.sys.sunrise.toLong()
                        val sunset=responseBody.sys.sunset.toLong()
                        val sealevel=responseBody.main.pressure
                        val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
                        val max=responseBody.main.temp_max
                        val min=responseBody.main.temp_min
                        binding.Temp.text="$temperature°C"
                        binding.weather.text=condition
                        binding.condition.text="$condition"
                        binding.Max.text="MAX Temp:$max°C"
                        binding.Min.text="Min Temp:$min°C"
                        binding.Humidity.text="$humidity"
                        binding.wind.text="$windspeed"
                        binding.sunrise.text="${time(sunrise)}"
                        binding.sunset.text="${time(sunset)}"
                        binding.sealevel.text="$sealevel"
                        binding.Day.text=dayName(System.currentTimeMillis())
                            binding.city.text="$city"
                            binding.Date.text=date()
                        changeimageaccordingTocondition(condition)




                    }
                }

                override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun changeimageaccordingTocondition(condition: String) {
            when(condition){
                "Clear Sky","Sunny","Clear"->{
                    binding.root.setBackgroundResource(R.drawable.sunny_background)
                    binding.lottieAnimationView.setAnimation(R.raw.sun)
                }
                "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                    binding.lottieAnimationView.setAnimation(R.raw.cloud)
                }
                "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain"->{
                    binding.root.setBackgroundResource(R.drawable.rain_background)
                    binding.lottieAnimationView.setAnimation(R.raw.rain)
                }
                "Light Snow","Moderate Snow","Heavy Snow","Blizzard","Snow",->{
                    binding.root.setBackgroundResource(R.drawable.snow_background)
                    binding.lottieAnimationView.setAnimation(R.raw.snow)
                }
               else ->{
                   binding.root.setBackgroundResource(R.drawable.sunny_background)
                   binding.lottieAnimationView.setAnimation(R.raw.sun)
               }
            }
            binding.lottieAnimationView.playAnimation()
    }
    private  fun time (timestamp:Long): String {
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun dayName(timestamp: Long):String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}



