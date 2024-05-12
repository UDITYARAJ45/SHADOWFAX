package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface WeatherAPI {
    @GET("weather")
    fun getWeather(
        @Query("q") cityName: String?,
        @Query("appid") apiKey: String?,
        @Query("units") units: String?
    ): Call<WeatherApp>
}
