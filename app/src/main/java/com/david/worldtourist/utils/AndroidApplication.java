package com.david.worldtourist.utils;


import android.app.Activity;
import android.app.Application;

import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.common.di.components.DaggerApplicationComponent;
import com.david.worldtourist.common.di.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class AndroidApplication extends Application {

    private RefWatcher refWatcher;
    private ApplicationComponent applicationComponent;

    public static AndroidApplication get(Activity activity) {
        return (AndroidApplication) activity.getApplication();
    }

    public RefWatcher getRefWatcher(){
        return refWatcher;
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        initializeLeakDetection();
        initializeInjector();
    }

    private void initializeLeakDetection(){
        if(LeakCanary.isInAnalyzerProcess(this))
            return;

        refWatcher = LeakCanary.install(this);
    }

    private void initializeInjector(){
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
