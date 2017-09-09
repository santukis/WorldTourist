package com.david.worldtourist.common.di.modules;


import android.content.Context;

import com.david.worldtourist.common.di.scopes.ApplicationScope;
import com.david.worldtourist.common.domain.UseCaseHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public Context context() {
        return context.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public UseCaseHandler useCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
