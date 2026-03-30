package com.ibtehazrafid.weatherroute;

public class WeatherPoint {
    private double temp;
    private double feelsLikeTemp;
    private int humidity;
    private double windSpeed;
    private String weatherCond;
    private String weatherIcon;
    private double precipChance;
    private double visibility;
    private long forecastTime;
    private double latitude;
    private double longitude;
    private int uvIndex;
    private int thunderstormProbability;
    private int cloudCover;
    private boolean isDaytime;
    private double windGust;
    private String windDirection;
    private int windDirectionDegrees;
    private String precipitationType;

    public WeatherPoint(double temp, double feelsLikeTemp, int humidity, double windSpeed, String weatherCond, String weatherIcon, double precipChance, double visibility, long forecastTime, double latitude, double longitude, int uvIndex, int thunderstormProbability, int cloudCover, boolean isDaytime, double windGust, String windDirection, int windDirectionDegrees, String precipitationType) {
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
        this.uvIndex = uvIndex;
        this.thunderstormProbability = thunderstormProbability;
        this.cloudCover = cloudCover;
        this.isDaytime = isDaytime;
        this.windGust = windGust;
        this.windDirection = windDirection;
        this.windDirectionDegrees = windDirectionDegrees;
        this.precipitationType = precipitationType;
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

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
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

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public int getThunderstormProbability() {
        return thunderstormProbability;
    }

    public void setThunderstormProbability(int thunderstormProbability) {
        this.thunderstormProbability = thunderstormProbability;
    }

    public int getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(int cloudCover) {
        this.cloudCover = cloudCover;
    }

    public boolean isDaytime() {
        return isDaytime;
    }

    public void setDaytime(boolean daytime) {
        isDaytime = daytime;
    }

    public double getWindGust() {
        return windGust;
    }

    public void setWindGust(double windGust) {
        this.windGust = windGust;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getWindDirectionDegrees() {
        return windDirectionDegrees;
    }

    public void setWindDirectionDegrees(int windDirectionDegrees) {
        this.windDirectionDegrees = windDirectionDegrees;
    }

    public String getPrecipitationType() {
        return precipitationType;
    }

    public void setPrecipitationType(String precipitationType) {
        this.precipitationType = precipitationType;
    }
}
