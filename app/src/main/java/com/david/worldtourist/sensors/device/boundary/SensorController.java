package com.david.worldtourist.sensors.device.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.sensors.domain.usecase.StartSensorService;

public interface SensorController {

    void connectSensor(@NonNull final StartSensorService.RequestValues requestValues,
                       @NonNull final UseCase.Callback<StartSensorService.ResponseValues> callback);

    void disconnectSensor();
}
