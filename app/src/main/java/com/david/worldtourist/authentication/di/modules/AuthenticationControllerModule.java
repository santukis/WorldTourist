package com.david.worldtourist.authentication.di.modules;


import android.app.Activity;


import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.authentication.data.boundary.AuthenticationController;
import com.david.worldtourist.authentication.data.controller.AuthenticationControllerImp;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthenticationControllerModule {

    private Activity activity;

    public AuthenticationControllerModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public AuthenticationController authenticationController() {
        return new AuthenticationControllerImp(activity);
    }
}
