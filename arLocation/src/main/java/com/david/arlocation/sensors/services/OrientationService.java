package com.david.arlocation.sensors.services;


import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.david.arlocation.sensors.boundary.SensorController;


public class OrientationService extends Service implements SensorEventListener, SensorController {

    private SensorManager sensorManager;
    private SensorListener orientationListener;

    ///////////////////////////Service LifeCycle///////////////////////////////
    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        orientationListener = null;
        unregisterSensors();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder() {
            @Override
            public SensorController getService() {
                return OrientationService.this;
            }
        };
    }

    ///////////////////SensorEventListener implementation/////////////////////
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrixFromVector = new float[16];

            SensorManager.getRotationMatrixFromVector(rotationMatrixFromVector, event.values);

            if (orientationListener != null)
                orientationListener.onSensorDataChanged(
                        SensorListener.ORIENTATION_SENSOR, rotationMatrixFromVector);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    ///////////////////SensorController implementation/////////////////////
    @Override
    public void connectSensor(@NonNull SensorListener sensorListener, @NonNull Class requestedService) {
        orientationListener = sensorListener;
        registerSensors();
    }

    @Override
    public void disconnectSensor() {
        orientationListener = null;
        unregisterSensors();
    }

    private void registerSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            orientationListener.isSensorAvailable(SensorListener.ORIENTATION_SENSOR, false);
        }
    }

    private void unregisterSensors() {
        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}