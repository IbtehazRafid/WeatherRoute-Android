package com.ibtehazrafid.weatherroute;

public class WeatherPoint {
    private double temp;
    private double feelsLikeTemp;
    private double humidity;
    private double windSpeed;
    private String weatherCond;
    private String weatherIcon;
    private double precipChance;
    private double visibility;
    private long forecastTime;
    private double latitude;
    private double longitude;

    public WeatherPoint(double temp, double feelsLikeTemp, double humidity, double windSpeed, String weatherCond, String weatherIcon, double precipChance, double visibility, long forecastTime, double latitude, double longitude) {
        this.temp = temp;
        this.feelsLikeTemp = feelsLikeTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCond = weatherCond;
        this.weatherIcon = weatherIcon;
        this.precipChance = precipChance;
        this.visibility = visibility;
        this.forecastTime = forecastTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherCond() {
        return weatherCond;
    }

    public void setWeatherCond(String weatherCond) {
        this.weatherCond = weatherCond;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public long getForecastTime() {
        return forecastTime;
    }

    public void setForecastTime(long forecastTime) {
        this.forecastTime = forecastTime;
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
}
