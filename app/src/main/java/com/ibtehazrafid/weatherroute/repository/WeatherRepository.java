package com.ibtehazrafid.weatherroute.repository;

import com.ibtehazrafid.weatherroute.BuildConfig;
import com.ibtehazrafid.weatherroute.WeatherPoint;
import com.ibtehazrafid.weatherroute.network.ApiClient;
import com.ibtehazrafid.weatherroute.network.WeatherApiService;
import com.ibtehazrafid.weatherroute.network.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private final WeatherApiService weatherApiService;
    private final String apiKey;

    public WeatherRepository() {
        this.apiKey = BuildConfig.MAPS_API_KEY;
        weatherApiService = ApiClient.getWeatherService();
    }

    public void getWeather(double latitude, double longitude, int hours, Callback<WeatherPoint> callback) {
        Call<WeatherResponse> call = weatherApiService.getWeatherForecast(apiKey, latitude, longitude, hours);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherPoint weatherPoint = convertToWeatherPoint(response.body(), latitude, longitude);
                    callback.onResponse(null, Response.success(weatherPoint));
                } else {
                    callback.onFailure(null, new Exception("Weather API error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }
    private WeatherPoint convertToWeatherPoint(WeatherResponse response, double latitude, double longitude) {
        WeatherResponse.ForecastHour hour = response.getForecastHours().get(0);
        return new WeatherPoint(
                hour.getTemperature().getDegrees(),
                hour.getFeelsLikeTemperature().getDegrees(),
                hour.getRelativeHumidity(),
                hour.getWind().getSpeed().getValue(),
                hour.getWeatherCondition().getType(),
                hour.getWeatherCondition().getIconBaseUri(),
                hour.getPrecipitation().getProbability().getPercent(),
                hour.getVisibility().getDistance(),
                System.currentTimeMillis(),
                latitude,
                longitude,
                hour.getUvIndex(),
                hour.getThunderstormProbability(),
                hour.getCloudCover(),
                hour.isDaytime(),
                hour.getWind().getGust().getValue(),
                hour.getWind().getDirection().getCardinal(),
                hour.getWind().getDirection().getDegrees(),
                hour.getPrecipitation().getProbability().getType());
    }
}
