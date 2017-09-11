package com.david.arlocation.view.controller;


import android.content.Context;
import android.support.annotation.NonNull;

import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.view.boundary.ArViewController;
import com.david.arlocation.view.model.DefaultMarkerRenderer;
import com.david.arlocation.view.model.Marker;
import com.david.arlocation.view.model.MarkerOptions;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.boundary.ArView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DefaultViewController<T extends ArItem> implements ArViewController<T> {

    private MarkerRenderer<T> markerRenderer;

    private ArView<T> iconsView;
    private ArView<T> radarView;

    private Set<Marker<T>> markers = new HashSet<>();

    public DefaultViewController(@NonNull Context context,
                                 @NonNull ArView<T> iconsView,
                                 @NonNull ArView<T> radarView) {
        this.iconsView = iconsView;
        this.radarView = radarView;
        this.markerRenderer = new DefaultMarkerRenderer<>(context);
    }

    @Override
    public void setMarkerRenderer(@NonNull MarkerRenderer<T> markerRenderer) {
        this.markerRenderer = markerRenderer;
    }

    @Override
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
        iconsView.setOnClusterClickListener(listener);
    }

    @Override
    public void setOnArItemClickListener(OnArItemClickListener<T> listener) {
        iconsView.setOnArItemClickListener(listener);
    }

    @Override
    public void createMarkers(Set<? extends Cluster<T>> clusters) {
        for (Cluster<T> cluster : clusters) {
            Marker<T> marker = new Marker<>(cluster);
            createMarkerOptions(marker);
            markers.add(marker);
        }

        iconsView.addMarkers(markers);
        radarView.addMarkers(markers);
    }

    private void createMarkerOptions(Marker<T> marker) {
        final int ONE_ITEM = 1;
        final int FIRST_ONE = 0;

        MarkerOptions markerOptions = new MarkerOptions();
        marker.setMarkerOptions(markerOptions);

        int markerSize = marker.getSize();

        if (markerSize == ONE_ITEM) {
            T item = new ArrayList<>(marker.getItems()).get(FIRST_ONE);
            markerRenderer.onBeforeArItemMarkerRenderer(item, markerOptions);

        } else {
            markerRenderer.onBeforeClusterMarkerRenderer(marker.getCluster(), markerOptions);
        }

    }

    @Override
    public void updateMarkersPosition() {
        iconsView.drawMarkers();
        radarView.drawMarkers();
    }

    @Override
    public void clearMarkers() {
        markers.clear();
        iconsView.clearMarkers();
        radarView.clearMarkers();
    }

}
