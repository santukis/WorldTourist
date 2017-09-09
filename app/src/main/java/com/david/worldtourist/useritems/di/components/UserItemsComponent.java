package com.david.worldtourist.useritems.di.components;

import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.useritems.di.modules.UserItemsPresenterModule;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsPresenter;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsView;

import dagger.Component;

@FragmentScope
@Component(modules = {UserItemsPresenterModule.class},
        dependencies = ApplicationComponent.class)
public interface UserItemsComponent {

    UserItemsPresenter<UserItemsView> getUserItemsPresenter();
}
