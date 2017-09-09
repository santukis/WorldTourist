package com.david.worldtourist.sensors.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.sensors.device.boundary.SensorController;

import javax.inject.Inject;

public class StopSensorService extends UseCase<StopSensorService.RequestValues, StopSensorService.ResponseValues> {


    private final SensorController sensorController;

    @Inject
    public StopSensorService(SensorController sensorController) {
        this.sensorController = sensorController;
    }

    @Override
    public void execute(StopSensorService.RequestValues requestValues) {
        sensorController.disconnectSensor();
    }

    @Override
    public void execute(StopSensorService.RequestValues requestValues,
                        Callback<StopSensorService.ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
