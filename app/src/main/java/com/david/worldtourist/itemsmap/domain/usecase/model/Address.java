package com.david.worldtourist.itemsmap.domain.usecase.model;


import com.david.worldtourist.items.domain.model.GeoCoordinate;

public class Address {

    private String name = "";
    private GeoCoordinate coordinates = GeoCoordinate.EMPTY_COORDINATE;

    public static Address EMPTY_ADDRESS = new Address();

    public Address() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoCoordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoCoordinate coordinates) {
        this.coordinates = coordinates;
    }
}
