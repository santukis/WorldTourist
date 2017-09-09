package com.david.worldtourist.itemsmap.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.itemsmap.domain.usecase.model.Address;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.Step;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;

import java.util.List;

public interface ItemsMapView extends FragmentView {

    void showStartingPosition(GeoCoordinate startingPosition);

    void showStartingItem(Item startingItem);

    void showAddressIcon(Address address);

    void clearMap();

    void clearMarkers();

    void showItems(List<Item> items);

    void loadItemDetail();

    void drawRoute(Route route);

    void showRouteView();

    void showRouteDestinationName(String destinationName);

    void showRouteIcon(TravelMode mode);

    void showRouteDistance(String distance);

    void showRouteDuration(String duration);

    void showRouteInstructions(List<Step> steps);

    void enableMic(boolean enabled);

}
