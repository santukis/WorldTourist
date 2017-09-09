package com.david.worldtourist.authentication.domain.usecase;


import android.content.Intent;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.AuthenticationController;

import javax.inject.Inject;

public class AuthResult extends UseCase<AuthResult.RequestValues, AuthResult.ResponseValues> {

    private final AuthenticationController controller;

    @Inject
    public AuthResult(AuthenticationController controller) {
        this.controller = controller;
    }


    @Override
    public void execute(RequestValues requestValues) {
        controller.onResult(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int requestCode;
        private int resultCode;
        private Intent data;

        public RequestValues(int requestCode, int resultCode, Intent data) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public int getResultCode() {
            return resultCode;
        }

        public Intent getData() {
            return data;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
