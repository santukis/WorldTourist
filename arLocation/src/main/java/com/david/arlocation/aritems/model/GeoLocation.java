package com.david.arlocation.aritems.model;

import java.util.Locale;

public class GeoLocation {

    private double latitude = 0;
    private double longitude = 0;
    private double altitude = 0;

    public final static GeoLocation EMPTY_COORDINATE = new GeoLocation(0,0,0);

    //Constructors
    public GeoLocation(){
        //Empty constructor for Firebase data read
    }
    public GeoLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocation(double latitude, double longitude, double altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    //Getters and Setters
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        String latitude = String.format(Locale.getDefault(), String.valueOf(this.latitude), "%.3f");
        String longitude = String.format(Locale.getDefault(), String.valueOf(this.longitude), "%.3f");

        return "Lat: " + latitude + ", Lng: " + longitude;
    }
}
