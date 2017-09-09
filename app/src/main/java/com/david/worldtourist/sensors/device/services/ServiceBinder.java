package com.david.worldtourist.sensors.device.services;

import android.os.Binder;

import com.david.worldtourist.sensors.device.boundary.SensorController;


public abstract class ServiceBinder extends Binder {

    public abstract SensorController getService();
}
