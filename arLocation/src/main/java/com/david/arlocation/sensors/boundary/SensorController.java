package com.david.arlocation.sensors.boundary;


import android.support.annotation.NonNull;

import com.david.arlocation.sensors.services.SensorListener;


public interface SensorController {

    void connectSensor(@NonNull SensorListener sensorListener, @NonNull Class service);

    void disconnectSensor();
}
