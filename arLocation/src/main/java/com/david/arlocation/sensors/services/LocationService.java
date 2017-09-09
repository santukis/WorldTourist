package com.david.arlocation.sensors.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.david.arlocation.aritems.model.GeoLocation;
import com.david.arlocation.sensors.boundary.SensorController;

public class LocationService extends Service implements LocationListener, SensorController {

    private static final long INTERVAL_BETWEEN_LOCATION_UPDATES = 30 * 1000; //milliseconds
    private static final float DISTANCE_BETWEEN_LOCATION_UPDATES = 50; //meters
    private static final long TIME_BETWEEN_LOCATION_UPDATES = 2 * 60 * 1000; //milliseconds


    private LocationManager locationManager;
    private SensorListener positionListener;
    private GeoLocation currentLocation = GeoLocation.EMPTY_COORDINATE;
    private GeoLocation previousLocation = GeoLocation.EMPTY_COORDINATE;

    /////////////////////////Service LifeCycle///////////////////////////////
    @Override
    public void onDestroy() {
        positionListener = null;
        providerDeactivation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder() {
            @Override
            public SensorController getService() {
                return LocationService.this;
            }
        };
    }
    ////////////////////LocationListener implementation//////////////////////

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = updatePosition(location);
        if (currentLocation != previousLocation && positionListener != null) {
            positionListener.onSensorDataChanged(SensorListener.LOCATION_SENSOR, currentLocation);
            previousLocation = currentLocation;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        activateProvider();
    }

    @Override
    public void onProviderEnabled(String s) {
        activateProvider();
    }

    @Override
    public void onProviderDisabled(String s) {
        positionListener.isSensorAvailable(SensorListener.LOCATION_SENSOR,
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    ///////////////////SensorController implementation/////////////////////
    @Override
    public void connectSensor(@NonNull SensorListener sensorListener, @NonNull Class requestedService) {
        positionListener = sensorListener;
        activateProvider();
    }

    @Override
    public void disconnectSensor() {
        positionListener = null;
        providerDeactivation();
    }

    ///////////////////////////GeoLocation Methods/////////////////////////////
    private void activateProvider() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        INTERVAL_BETWEEN_LOCATION_UPDATES,
                        DISTANCE_BETWEEN_LOCATION_UPDATES,
                        this);

                onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        INTERVAL_BETWEEN_LOCATION_UPDATES,
                        DISTANCE_BETWEEN_LOCATION_UPDATES,
                        this);

                onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

            } else if (positionListener != null) {
                positionListener.isSensorAvailable(SensorListener.LOCATION_SENSOR, false);
            }

        }
    }

    private void providerDeactivation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null)
                locationManager.removeUpdates(this);
        }
    }

    private GeoLocation updatePosition(Location location) {
        if (location != null && (currentLocation == GeoLocation.EMPTY_COORDINATE
                || System.currentTimeMillis() - location.getTime() > TIME_BETWEEN_LOCATION_UPDATES)) {
            return new GeoLocation(location.getLatitude(), location.getLongitude());
        }
        return currentLocation;
    }

}
