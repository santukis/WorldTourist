package com.david.worldtourist.authentication.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.domain.usecase.AuthResult;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;

public interface AuthenticationController {

    void login(@NonNull DoLogin.RequestValues requestValues,
               @NonNull UseCase.Callback<DoLogin.ResponseValues> callback);

    void onResult(@NonNull AuthResult.RequestValues requestValues);
}
