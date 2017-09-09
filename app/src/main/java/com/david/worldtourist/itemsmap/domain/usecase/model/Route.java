package com.david.worldtourist.itemsmap.domain.usecase.model;


import com.david.worldtourist.items.domain.model.GeoCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private String name = "";
    private String distance = "";
    private String duration = "";
    private TravelMode travelMode = TravelMode.NONE;
    private List<GeoCoordinate> routePoints = new ArrayList<>();
    private List<Step> routeSteps = new ArrayList<>();

    public static Route EMPTY_ROUTE = new Route();

    public Route() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public List<GeoCoordinate> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<GeoCoordinate> routePoints) {
        this.routePoints = routePoints;
    }

    public List<Step> getRouteSteps() {
        return routeSteps;
    }

    public void setRouteSteps(List<Step> routeSteps) {
        this.routeSteps = routeSteps;
    }
}
