package com.david.arlocation.sensors.services;

import android.os.Binder;

import com.david.arlocation.sensors.boundary.SensorController;


public abstract class ServiceBinder extends Binder {

    public abstract SensorController getService();
}
