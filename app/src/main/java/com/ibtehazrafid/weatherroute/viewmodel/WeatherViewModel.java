package com.ibtehazrafid.weatherroute.viewmodel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.app.Application;

import com.ibtehazrafid.weatherroute.WeatherPoint;
import com.ibtehazrafid.weatherroute.repository.WeatherRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository weatherRepository;
    private final MutableLiveData<WeatherPoint> weatherPoint = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public WeatherViewModel(Application application) {
        super(application);
        weatherRepository = new WeatherRepository();
    }
    public MutableLiveData<WeatherPoint> getWeatherPoint() {
        return weatherPoint;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMesg() {
        return errorMsg;
    }

    public void fetchWeather(double latitude, double longitude, int hours) {
        isLoading.setValue(true);
        weatherRepository.getWeather(latitude, longitude, hours, new Callback<WeatherPoint>() {
            @Override
            public void onResponse(Call<WeatherPoint> call, Response<WeatherPoint> response) {
                if (response.body() != null) {
                    weatherPoint.postValue(response.body());
                    isLoading.postValue(false);
                } else {
                    errorMsg.postValue("Weather API error: " + response.code());
                    isLoading.postValue(false);
                }
            }
            @Override
            public void onFailure(Call<WeatherPoint> call, Throwable t) {
                errorMsg.postValue(t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

}
