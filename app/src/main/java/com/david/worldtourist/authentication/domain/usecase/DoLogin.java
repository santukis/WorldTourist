package com.david.worldtourist.authentication.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.AuthenticationController;
import com.david.worldtourist.authentication.domain.model.ProviderFilter;

import javax.inject.Inject;

public class DoLogin extends UseCase<DoLogin.RequestValues, DoLogin.ResponseValues> {

    private final AuthenticationController controller;

    @Inject
    public DoLogin(AuthenticationController controller) {
        this.controller = controller;
    }


    @Override
    public void execute(RequestValues requestValues) {
        controller.login(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                getCallback().onSuccess(response);
            }

            @Override
            public void onError(String error) {
                getCallback().onError(error);
            }
        });
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private ProviderFilter provider;

        public RequestValues(ProviderFilter provider) {
            this.provider = provider;
        }

        public ProviderFilter getProvider() {
            return provider;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
