package com.david.worldtourist.aritems.presentation.components;

import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.aritems.model.GeoLocation;
import com.david.worldtourist.aritems.presentation.adapter.ClusterItemAdapter;
import com.david.worldtourist.items.domain.model.Item;


public class AItem implements ArItem, ClusterItemAdapter.ItemAdapter {

    private final Item item;

    public AItem(Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public GeoLocation getLocation() {
        return new GeoLocation(
                item.getCoordinate().getLatitude(), item.getCoordinate().getLongitude());
    }
}
