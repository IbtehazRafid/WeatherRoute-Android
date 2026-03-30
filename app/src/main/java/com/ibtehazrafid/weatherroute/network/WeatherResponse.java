package com.ibtehazrafid.weatherroute.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("forecastHours")
    private List<ForecastHour> forecastHours;

    public List<ForecastHour> getForecastHours() {
        return forecastHours;
    }

    public static class ForecastHour {
        @SerializedName("relativeHumidity")
        private int relativeHumidity;
        @SerializedName("uvIndex")
        private int uvIndex;
        @SerializedName("thunderstormProbability")
        private int thunderstormProbability;
        @SerializedName("cloudCover")
        private int cloudCover;
        @SerializedName("isDaytime")
        private boolean isDaytime;
        @SerializedName("temperature")
        private Temperature temperature;
        @SerializedName("feelsLikeTemperature")
        private Temperature feelsLikeTemperature;
        @SerializedName("wind")
        private Wind wind;
        @SerializedName("precipitation")
        private Precipitation precipitation;
        @SerializedName("visibility")
        private Visibility visibility;
        @SerializedName("weatherCondition")
        private WeatherCondition weatherCondition;
        @SerializedName("interval")
        private Interval interval;
        public int getRelativeHumidity() {
            return relativeHumidity;
        }
        public int getUvIndex() {
            return uvIndex;
        }
        public int getThunderstormProbability() {
            return thunderstormProbability;
        }
        public int getCloudCover() {
            return cloudCover;
        }
        public boolean isDaytime() {
            return isDaytime;
        }
        public Temperature getTemperature() {
            return temperature;
        }
        public Temperature getFeelsLikeTemperature() {
            return feelsLikeTemperature;
        }
        public Wind getWind() {
            return wind;
        }
        public Precipitation getPrecipitation() {
            return precipitation;
        }
        public Visibility getVisibility() {
            return visibility;
        }
        public WeatherCondition getWeatherCondition() {
            return weatherCondition;
        }
        public Interval getInterval() {
            return interval;
        }
    }

    public static class Temperature {
        @SerializedName("degrees")
        private double degrees;
        @SerializedName("unit")
        private String unit;
        public double getDegrees() {
            return degrees;
        }
        public String getUnit() {
            return unit;
        }
    }
    public static class WeatherCondition {
        @SerializedName("iconBaseUri")
        private String iconBaseUri;
        @SerializedName("type")
        private String type;
        public String getIconBaseUri() {
            return iconBaseUri;
        }
        public String getType() {
            return type;
        }
    }
    public static class Precipitation {
        @SerializedName("probability")
        private PrecipitationProbability probability;
        public PrecipitationProbability getProbability() {
            return probability;
        }
    }
    public static class PrecipitationProbability {
        @SerializedName("type")
        private String type;
        @SerializedName("percent")
        private int percent;
        public int getPercent() {
            return percent;
        }
        public String getType() {
            return type;
        }
    }
    public static class Wind {
        @SerializedName("direction")
        private WindDirection direction;
        @SerializedName("speed")
        private WindSpeed speed;
        @SerializedName("gust")
        private WindSpeed gust;
        public WindDirection getDirection() {
            return direction;
        }
        public WindSpeed getSpeed() {
            return speed;
        }
        public WindSpeed getGust() {
            return gust;
        }
    }
    public static class WindDirection {
        @SerializedName("cardinal")
        private String cardinal;
        @SerializedName("degrees")
        private int degrees;
        public String getCardinal() {
            return cardinal;
        }
        public int getDegrees() {
            return degrees;
        }
    }
    public static class WindSpeed {
        @SerializedName("unit")
        private String unit;
        @SerializedName("value")
        private double value;
        public String getUnit() {
            return unit;
        }
        public double getValue() {
            return value;
        }
    }
    public static class Visibility {
        @SerializedName("unit")
        private String unit;
        @SerializedName("distance")
        private double distance;
        public String getUnit() {
            return unit;
        }
        public double getDistance() {
            return distance;
        }
    }
    public static class Interval {
        @SerializedName("startTime")
        private String startTime;
        @SerializedName("endTime")
        private String endTime;
        public String getStartTime() {
            return startTime;
        }
        public String getEndTime() {
            return endTime;
        }
    }
}

