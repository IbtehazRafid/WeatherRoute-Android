package com.ibtehazrafid.weatherroute.viewmodel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.app.Application;

import com.ibtehazrafid.weatherroute.RoutePoint;
import com.ibtehazrafid.weatherroute.network.RoutesRequest;
import com.ibtehazrafid.weatherroute.repository.RouteRepository;

import java.util.List;

public class RouteViewModel extends AndroidViewModel {
    private final RouteRepository routeRepository;
    private final MutableLiveData<List<RoutePoint>> routePoints = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public RouteViewModel(Application application) {
        super(application);
        routeRepository = new RouteRepository();
    }

    public MutableLiveData<List<RoutePoint>> getRoutePoints() {
        return routePoints;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMesg() {
        return errorMsg;
    }

    public void computeRoute(RoutesRequest request, String fieldMask) {
        isLoading.setValue(true);
        routeRepository.getRouteWeather(request, fieldMask, new RouteRepository.RouteCallback() {
            @Override
            public void onSuccess(List<RoutePoint> points) {
                routePoints.postValue(points);
                isLoading.postValue(false);
            }
            @Override
            public void onFailure(Exception e) {
                errorMsg.postValue(e.getMessage());
                isLoading.postValue(false);
            }
        });

    }
}
