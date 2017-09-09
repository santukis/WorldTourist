package com.david.worldtourist.authentication.data.controller;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.AuthenticationController;
import com.david.worldtourist.authentication.domain.usecase.AuthResult;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.authentication.data.firebaseAPI.FacebookLogin;
import com.david.worldtourist.authentication.data.firebaseAPI.GoogleLogin;
import com.david.worldtourist.authentication.data.boundary.Login;
import com.david.worldtourist.authentication.domain.model.ProviderFilter;
import com.david.worldtourist.authentication.data.firebaseAPI.TwitterLogin;

public class AuthenticationControllerImp implements AuthenticationController {

    private Login login;
    private Activity activity;

    public AuthenticationControllerImp(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void login(@NonNull DoLogin.RequestValues requestValues,
                      @NonNull UseCase.Callback<DoLogin.ResponseValues> callback) {

        ProviderFilter provider = requestValues.getProvider();

        switch (provider) {
            case FACEBOOK:
                login = new FacebookLogin(activity, callback);
                break;

            case TWITTER:
                login = new TwitterLogin(activity, callback);
                break;

            case GOOGLE:
                login = new GoogleLogin(activity, callback);
                break;
        }

        if (login != null) {
            login.login();
        }

    }

    @Override
    public void onResult(@NonNull AuthResult.RequestValues requestValues) {

        int requestCode = requestValues.getRequestCode();
        int resultCode = requestValues.getResultCode();
        Intent data = requestValues.getData();

        login.onResult(requestCode, resultCode, data);
    }
}
