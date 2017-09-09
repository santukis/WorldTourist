package com.david.worldtourist.items.di.components;

import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.items.di.modules.ItemsPresenterModule;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;
import com.david.worldtourist.items.presentation.boundary.ItemsView;

import dagger.Component;

@FragmentScope
@Component(modules = {ItemsPresenterModule.class},
        dependencies = ApplicationComponent.class)
public interface ItemsComponent {

    ItemsPresenter<ItemsView> getItemsPresenter();

}