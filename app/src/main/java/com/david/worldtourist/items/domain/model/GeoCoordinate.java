package com.david.worldtourist.items.domain.model;

import java.io.Serializable;
import java.util.Locale;

public class GeoCoordinate implements Serializable {

    private double latitude = 0;
    private double longitude = 0;
    private double altitude = 0;

    public final static GeoCoordinate EMPTY_COORDINATE = new GeoCoordinate(0,0,0);

    //Constructors
    public GeoCoordinate(){
        //Empty constructor for Firebase data read
    }
    public GeoCoordinate (double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoCoordinate(double latitude, double longitude, double altitude){
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

    public static double getDistance(GeoCoordinate userPosition, GeoCoordinate other) {
        final double WGS84_A = 6_378_137;
        final double WGS84_B = 6_356_752;

        final double EARTH_RADIUS = Math.hypot((Math.cos(userPosition.getLatitude()) * WGS84_A),
                (Math.sin(userPosition.getLatitude()) * WGS84_B));

        double latDistance = Math.toRadians(userPosition.getLatitude() - other.getLatitude());
        double longDistance = Math.toRadians(userPosition.getLongitude() - other.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.sin(longDistance / 2) * Math.sin(longDistance / 2)
                * Math.cos(Math.toRadians(userPosition.getLatitude()))
                * Math.cos(Math.toRadians(other.getLatitude()));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * EARTH_RADIUS;
    }

    @Override
    public String toString() {
        String latitude = String.format(Locale.getDefault(), String.valueOf(this.latitude), "%.3f");
        String longitude = String.format(Locale.getDefault(), String.valueOf(this.longitude), "%.3f");

        return "Lat: " + latitude + ", Lng: " + longitude;
    }
}
