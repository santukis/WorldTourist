package com.david.arlocation.sensors.services;

public interface SensorListener {

    int LOCATION_SENSOR = 0;
    int ORIENTATION_SENSOR = 1;

    void onSensorDataChanged(int sensorId, Object data);

    void isSensorAvailable(int sensorId, boolean available);
}
