package com.ibtehazrafid.weatherroute.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface WeatherApiService {
    @GET("v1/forecast/hours:lookup")
    Call<WeatherResponse> getWeatherForecast(
            @Query("key") String apiKey,
            @Query("location.latitude") double latitude,
            @Query("location.longitude") double longitude,
            @Query("hours") int hours
    );
}