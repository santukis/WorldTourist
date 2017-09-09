package com.david.worldtourist.itemsmap.di.components;


import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.itemsmap.di.modules.ItemsMapPresenterModule;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapPresenter;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapView;

import dagger.Component;

@FragmentScope
@Component(modules = ItemsMapPresenterModule.class,
        dependencies = ApplicationComponent.class)
public interface ItemsMapComponent {

    ItemsMapPresenter<ItemsMapView> getItemsMapPresenter();
}
