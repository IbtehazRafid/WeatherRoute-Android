package com.ibtehazrafid.weatherroute.repository;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.ibtehazrafid.weatherroute.BuildConfig;
import com.ibtehazrafid.weatherroute.RoutePoint;
import com.ibtehazrafid.weatherroute.network.ApiClient;
import com.ibtehazrafid.weatherroute.network.RoutesRequest;
import com.ibtehazrafid.weatherroute.network.RoutesResponse;
import com.ibtehazrafid.weatherroute.network.WeatherApiService;
import com.ibtehazrafid.weatherroute.network.WeatherResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteRepository {
    private final WeatherApiService weatherApiService;
    private final String apiKey;
    public RouteRepository() {
        this.weatherApiService = ApiClient.getWeatherService();
        this.apiKey = BuildConfig.MAPS_API_KEY;
    }

    public void getRouteWeather(RoutesRequest request, String fieldMask, RouteCallback callback) {
        ApiClient.getRoutesService().computeRoutes(request, this.apiKey, fieldMask).enqueue(new Callback<RoutesResponse>() {
            @Override
            public void onResponse(Call<RoutesResponse> call, Response<RoutesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processRouteResponse(response.body(), callback);
                } else {
                    callback.onFailure(new Exception("Route API error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<RoutesResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    private void processRouteResponse(RoutesResponse response, RouteCallback callback) {
        if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
            callback.onFailure(new Exception("No routes found"));
            return;
        }
        RoutesResponse.Route route = response.getRoutes().get(0);
        List<RoutesResponse.RouteLeg> legs = route.getLegs();
        if (legs == null || legs.isEmpty()) {
            callback.onFailure(new Exception("Route has no legs"));
            return;
        }
        List <RoutePointWithEta> pointsToFetchWeather = new ArrayList<>();
        long accumulatedSecondsDuration = 0;
        for (RoutesResponse.RouteLeg leg : legs) {
            if (leg.getPolyline() == null) {
                continue;
            }
            String encodedPolyline = leg.getPolyline().getEncodedPolyline();
            List<LatLng> legPath = PolyUtil.decode(encodedPolyline);
            long legsSecondsDuration = parseDuration(leg.getDuration());
            int interval = Math.max(1, (int) (legsSecondsDuration / legPath.size() / 1800));
            if (interval > 20) {
                interval = 20;
            }
            for (int i = 0; i < legPath.size(); i += interval) {
                LatLng latLng = legPath.get(i);
                long etaOffset = accumulatedSecondsDuration + (long) ((double) i/legPath.size() * legsSecondsDuration);
                pointsToFetchWeather.add(new RoutePointWithEta(latLng, etaOffset));
            }
            accumulatedSecondsDuration += legsSecondsDuration;
        }
        RoutesResponse.RouteLeg lastLeg = legs.get(legs.size() - 1);
        if (lastLeg.getPolyline() != null) {
            List<LatLng> lastLegPath = PolyUtil.decode(lastLeg.getPolyline().getEncodedPolyline());
            if (!lastLegPath.isEmpty()) {
                pointsToFetchWeather.add(new RoutePointWithEta(lastLegPath.get(lastLegPath.size() - 1), accumulatedSecondsDuration));
            }
            fetchWeatherForPoints(pointsToFetchWeather, callback);
        }
    }

    private void fetchWeatherForPoints(List<RoutePointWithEta> points, RouteCallback callback) {
        final String key = this.apiKey;
        List<RoutePoint> results = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger remaining = new AtomicInteger(points.size());
        if (points.isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }
        long startTimeMillis = System.currentTimeMillis();
        for (RoutePointWithEta pointWithEta : points) {
            LatLng latLng = pointWithEta.latLng;
            Long etaSeconds = pointWithEta.etaSeconds;
            int hourOffset = (int) (etaSeconds / 3600) + 1;
            weatherApiService.getWeatherForecast(apiKey, latLng.latitude, latLng.longitude, hourOffset).enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getForecastHours() != null) {
                        List<WeatherResponse.ForecastHour> forecasts = response.body().getForecastHours();
                        if (!forecasts.isEmpty()) {
                            int index = Math.min(hourOffset - 1, forecasts.size() - 1);
                            WeatherResponse.ForecastHour forecast = forecasts.get(index);
                            String condition = forecast.getWeatherCondition() != null ? forecast.getWeatherCondition().getType() : "Unknown";
                            long arrivalTime = startTimeMillis + (etaSeconds * 1000);
                            results.add(new RoutePoint(latLng.latitude, latLng.longitude, condition, arrivalTime));
                        }
                    }
                    if(remaining.decrementAndGet() == 0) {
                        callback.onSuccess(new ArrayList<>(results));
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    if(remaining.decrementAndGet() == 0) {
                        callback.onSuccess(new ArrayList<>(results));
                    }
                }
            });

        }
    }

    private long parseDuration(String durationStr) {
        if (durationStr == null || !durationStr.endsWith("s")) {
            return 0;
        }
        try {
            return Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static class RoutePointWithEta {
        LatLng latLng;
        long etaSeconds;
        public RoutePointWithEta(LatLng latLng, long etaSeconds) {
            this.latLng = latLng;
            this.etaSeconds = etaSeconds;
        }
    }

    public interface RouteCallback {
        void onSuccess(List<RoutePoint> points);
        void onFailure(Exception e);
    }

}
