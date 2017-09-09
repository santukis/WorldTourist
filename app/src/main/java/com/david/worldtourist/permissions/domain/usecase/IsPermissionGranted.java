package com.david.worldtourist.permissions.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.permissions.device.boundary.PermissionController;

import javax.inject.Inject;


public class IsPermissionGranted extends UseCase<IsPermissionGranted.RequestValues, IsPermissionGranted.ResponseValues> {

    private final PermissionController controller;

    @Inject
    public IsPermissionGranted(PermissionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
        controller.isPermissionGranted(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String permission;

        public RequestValues(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private boolean isGranted;

        public ResponseValues(boolean isGranted) {
            this.isGranted = isGranted;
        }

        public boolean isGranted() {
            return isGranted;
        }
    }
}
