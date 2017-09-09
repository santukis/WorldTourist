package com.david.worldtourist.permissions.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.permissions.device.boundary.PermissionController;

import javax.inject.Inject;

public class IsNetworkAvailable extends UseCase<IsNetworkAvailable.RequestValues, IsNetworkAvailable.ResponseValues> {

    private final PermissionController controller;

    @Inject
    public IsNetworkAvailable(PermissionController controller) {
        this.controller = controller;
    }


    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return new ResponseValues(controller.isNetworkAvailable());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private boolean isAvailable;

        public ResponseValues(boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public boolean isAvailable() {
            return isAvailable;
        }
    }
}
