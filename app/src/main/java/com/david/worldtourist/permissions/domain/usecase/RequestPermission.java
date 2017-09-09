package com.david.worldtourist.permissions.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.permissions.device.boundary.PermissionController;

import javax.inject.Inject;

public class RequestPermission extends UseCase<RequestPermission.RequestValues, RequestPermission.ResponseValues> {

    private final PermissionController controller;

    @Inject
    public RequestPermission(PermissionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute(RequestValues requestValues) {
        controller.requestPermission(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String permission;
        private int requestCode;

        public RequestValues(String permission, int requestCode) {
            this.permission = permission;
            this.requestCode = requestCode;
        }

        public String getPermission() {
            return permission;
        }

        public int getRequestCode() {
            return requestCode;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
