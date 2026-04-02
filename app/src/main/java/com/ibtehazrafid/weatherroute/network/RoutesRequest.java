package com.ibtehazrafid.weatherroute.network;

import java.util.List;

public class RoutesRequest {
    private Waypoint origin;
    private Waypoint destination;
    private List<Waypoint> intermediates;
    private String travelMode;
    private String routingPreference;
    private String departureTime;
    private boolean computeAlternativeRoutes;
    private String trafficModel;
    public RoutesRequest(Waypoint origin, Waypoint destination, List<Waypoint> intermediates, String travelMode, String departureTime) {
        this.origin = origin;
        this.destination = destination;
        this.intermediates = intermediates;
        this.travelMode = travelMode;
        this.routingPreference = "TRAFFIC_AWARE";
        this.departureTime = departureTime;
        this.computeAlternativeRoutes = false;
        this.trafficModel = "BEST_GUESS";
    }

    public static class LatLng {
        private double latitude;
        private double longitude;
        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public static class Location {
        private LatLng latLng;
        public Location(LatLng latLng) {
            this.latLng = latLng;
        }
    }

    public static class Waypoint {
        private Location location;
        public Waypoint(Location location) {
            this.location = location;
        }
    }

}
