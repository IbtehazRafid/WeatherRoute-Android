package com.ibtehazrafid.weatherroute.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String ROUTES_BASE_URL = "https://routes.googleapis.com/";
    private static final String WEATHER_BASE_URL = "https://weather.googleapis.com/";
    private static Retrofit routesRetrofit;
    private static Retrofit weatherRetrofit;
    public static Retrofit getRoutesClient() {
        if (routesRetrofit == null) {
            routesRetrofit = new Retrofit.Builder()
                    .baseUrl(ROUTES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return routesRetrofit;
    }
    public static Retrofit getWeatherClient() {
        if (weatherRetrofit == null) {
            weatherRetrofit = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return weatherRetrofit;
    }
    public static RoutesApiService getRoutesService() {
        return getRoutesClient().create(RoutesApiService.class);
    }
    public static WeatherApiService getWeatherService() {
        return getWeatherClient().create(WeatherApiService.class);
    }
}
