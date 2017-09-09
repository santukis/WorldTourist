package com.david.worldtourist.sensors.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.sensors.device.boundary.SensorController;
import com.david.worldtourist.sensors.device.services.SensorListener;

import javax.inject.Inject;

public class StartSensorService extends UseCase<StartSensorService.RequestValues, StartSensorService.ResponseValues> {


    private final SensorController sensorController;

    @Inject
    public StartSensorService(SensorController sensorController) {
        this.sensorController = sensorController;
    }

    @Override
    public void execute(StartSensorService.RequestValues requestValues) {
    }

    @Override
    public void execute(StartSensorService.RequestValues requestValues,
                        final Callback<StartSensorService.ResponseValues> callback) {
        sensorController.connectSensor(requestValues, callback);
    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Class service;
        private SensorListener sensorListener;

        public RequestValues(Class sensorServiceClass, SensorListener sensorListener) {
            service = sensorServiceClass;
            this.sensorListener = sensorListener;
        }

        public Class getService(){
            return service;
        }

        public SensorListener getSensorListener() {
            return sensorListener;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
