package com.david.worldtourist.items.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;

import java.util.List;

public interface ItemsView extends FragmentView{

    void showItems(List<Item> items, GeoCoordinate userPosition);

    void showInfoMessage(int textReference, int iconReference);

    void hideInfoMessage();

    void enableMic(boolean enabled);

}
