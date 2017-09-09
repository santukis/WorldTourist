package com.david.worldtourist.sensors.device.controller;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.sensors.device.boundary.SensorController;
import com.david.worldtourist.sensors.device.services.ServiceBinder;
import com.david.worldtourist.sensors.domain.usecase.StartSensorService;

public class SensorControllerImp implements SensorController {

    private Context context;
    private ServiceConnection serviceConnection;

    public SensorControllerImp(Context context) {
        this.context = context;
    }

    @Override
    public void connectSensor(@NonNull final StartSensorService.RequestValues requestValues,
                              @NonNull final UseCase.Callback<StartSensorService.ResponseValues> callback) {
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    (((ServiceBinder) service).getService()).connectSensor(requestValues, callback);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
        }
        context.bindService(
                new Intent(context, requestValues.getService()),
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
