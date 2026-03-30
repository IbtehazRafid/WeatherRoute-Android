package com.ibtehazrafid.weatherroute;

public class RoutePoint {
    private double latitude;
    private double longitude;
    private String weather;
    private long estimatedArrivalTime;

    public RoutePoint(double longitude, double latitude, String weather, long estimatedArrivalTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weather = weather;
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public long getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(long estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }
}