package com.david.worldtourist.sensors.di.modules;

import android.content.Context;

import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.sensors.device.boundary.SensorController;
import com.david.worldtourist.sensors.device.controller.SensorControllerImp;

import dagger.Module;
import dagger.Provides;

@Module
public class SensorControllerModule {

    @Provides
    @FragmentScope
    public SensorController sensorController(Context context) {
        return new SensorControllerImp(context);
    }
}