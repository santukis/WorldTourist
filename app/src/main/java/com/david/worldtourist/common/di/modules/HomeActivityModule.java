package com.david.worldtourist.common.di.modules;


import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.common.presentation.navigation.Navigator;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {

    private final HomeActivity homeActivity;

    public HomeActivityModule(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }

    @Provides
    @ActivityScope
    public HomeActivity homeActivity(){
        return this.homeActivity;
    }

    @Provides
    @ActivityScope
    public Navigator navigator(){
        return new Navigator(homeActivity);
    }
}
