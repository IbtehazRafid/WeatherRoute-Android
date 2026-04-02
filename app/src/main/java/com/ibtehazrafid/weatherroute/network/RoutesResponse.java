package com.ibtehazrafid.weatherroute.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutesResponse {
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        @SerializedName("distanceMeters")
        private int distanceMeters;
        @SerializedName("duration")
        private String duration;
        @SerializedName("staticDuration")
        private String staticDuration;
        @SerializedName("polyline")
        private Polyline polyline;
        @SerializedName("legs")
        private List<RouteLeg> legs;
        public int getDistanceMeters() {
            return distanceMeters;
        }
        public String getDuration() {
            return duration;
        }
        public String getStaticDuration() {
            return staticDuration;
        }
        public Polyline getPolyline() {
            return polyline;
        }
        public List<RouteLeg> getLegs() {
            return legs;
        }
    }

    public static class RouteLeg {
        @SerializedName("distanceMeters")
        private int distanceMeters;
        @SerializedName("duration")
        private String duration;
        @SerializedName("staticDuration")
        private String staticDuration;
        @SerializedName("polyline")
        private Polyline polyline;
        @SerializedName("startLocation")
        private Location startLocation;
        @SerializedName("endLocation")
        private Location endLocation;
        public int getDistanceMeters() {
            return distanceMeters;
        }
        public String getDuration() {
            return duration;
        }
        public String getStaticDuration() {
            return staticDuration;
        }
        public Polyline getPolyline() {
            return polyline;
        }
        public Location getStartLocation() {
            return startLocation;
        }
        public Location getEndLocation() {
            return endLocation;
        }
    }

    public static class Polyline {
        @SerializedName("encodedPolyline")
        private String encodedPolyline;
        public String getEncodedPolyline() {
            return encodedPolyline;
        }
    }

    public static class Location {
        @SerializedName("latLng")
        private LatLng latLng;
        public LatLng getLatLng() {
            return latLng;
        }
    }

    public static class LatLng {
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;
        public double getLatitude() {
            return latitude;
        }
        public double getLongitude() {
            return longitude;
        }
    }
}
