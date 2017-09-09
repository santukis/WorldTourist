package com.david.worldtourist.aritems.di.components;


import com.david.worldtourist.aritems.di.modules.ArItemsPresenterModule;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsPresenter;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsView;
import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;

import dagger.Component;

@FragmentScope
@Component(modules = ArItemsPresenterModule.class,
        dependencies = ApplicationComponent.class)
public interface ArItemsComponent {

    ArItemsPresenter<ArItemsView> getArItemsPresenter();
}
