package com.david.worldtourist.common.di.components;


import com.david.worldtourist.common.di.modules.HomeActivityModule;
import com.david.worldtourist.common.di.modules.HomePresenterModule;
import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.common.presentation.activity.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {HomeActivityModule.class, HomePresenterModule.class},
        dependencies = {ApplicationComponent.class})
public interface ActivityComponent {

    void inject(HomeActivity homeActivity);

}