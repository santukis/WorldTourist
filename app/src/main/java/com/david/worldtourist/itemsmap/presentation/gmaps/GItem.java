package com.david.worldtourist.itemsmap.presentation.gmaps;

import com.david.worldtourist.aritems.presentation.adapter.ClusterItemAdapter;
import com.david.worldtourist.items.domain.model.Item;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class GItem implements ClusterItem, ClusterItemAdapter.ItemAdapter {

    private final Item item;

    public GItem(Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(item.getCoordinate().getLatitude(),
                item.getCoordinate().getLongitude());
    }


}
