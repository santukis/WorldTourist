package com.david.worldtourist.authentication.di.components;


import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.authentication.di.modules.LoginPresenterModule;
import com.david.worldtourist.authentication.presentation.boundary.LoginPresenter;
import com.david.worldtourist.authentication.presentation.boundary.LoginView;

import dagger.Component;

@ActivityScope
@Component(modules = {LoginPresenterModule.class},
        dependencies = ApplicationComponent.class)
public interface LoginComponent {

    LoginPresenter<LoginView> getLoginPresenter();
}
