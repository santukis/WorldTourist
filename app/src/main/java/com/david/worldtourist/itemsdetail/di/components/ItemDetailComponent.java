package com.david.worldtourist.itemsdetail.di.components;

import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.itemsdetail.di.modules.ItemDetailPresenterModule;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailPresenter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailView;

import dagger.Component;

@FragmentScope
@Component(modules = {ItemDetailPresenterModule.class},
        dependencies = {ApplicationComponent.class})
public interface ItemDetailComponent {

    ItemDetailPresenter<ItemDetailView> getItemsDetailPresenter();
}
