package com.david.worldtourist.common.di.components;


import android.content.Context;

import com.david.worldtourist.common.di.modules.ApplicationModule;
import com.david.worldtourist.common.di.scopes.ApplicationScope;
import com.david.worldtourist.common.domain.UseCaseHandler;

import dagger.Component;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getContext();

    UseCaseHandler getUseCaseHandler();

}
