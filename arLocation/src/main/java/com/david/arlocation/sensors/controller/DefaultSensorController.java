package com.david.arlocation.sensors.controller;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.david.arlocation.sensors.services.SensorListener;
import com.david.arlocation.sensors.services.ServiceBinder;
import com.david.arlocation.sensors.boundary.SensorController;


public class DefaultSensorController implements SensorController {

    private Context context;
    private ServiceConnection serviceConnection;

    public DefaultSensorController(Context context) {
        this.context = context;
    }

    @Override
    public void connectSensor(@NonNull final SensorListener sensorListener, @NonNull final Class requestedService) {
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    (((ServiceBinder) service).getService()).connectSensor(sensorListener, requestedService);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
        }
        context.bindService(
                new Intent(context, requestedService),
                serviceConnection,
                Service.BIND_AUTO_CREATE);
    }

    @Override
    public void disconnectSensor(){
        if (serviceConnection != null) {
            context.unbindService(serviceConnection);
            serviceConnection = null;
        }
    }
}
