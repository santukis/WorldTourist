package com.david.worldtourist.itemsmap.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.ItemsSearchPresenter;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;

public interface ItemsMapPresenter<T extends BaseView> extends ItemsSearchPresenter<T> {

    void loadStartingPosition();

    void loadItems(GeoCoordinate coordinates);

    void loadRoute(Item item, GeoCoordinate origin, TravelMode travelMode);

    void refreshRoute(Route route);

    void deleteRoute(Route route);
}