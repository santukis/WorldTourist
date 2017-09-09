package com.david.arlocation.view.model;


import android.support.annotation.NonNull;

import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.CameraPosition;
import com.david.arlocation.cluster.model.Cluster;

import java.util.Collection;

public class Marker<T extends ArItem> {

    private Cluster<T> cluster;
    private MarkerOptions markerOptions;

    public Marker(@NonNull Cluster<T> cluster) {
        this.cluster = cluster;
    }

    public Cluster<T> getCluster() {
        return cluster;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public Collection<T> getItems() {
        return cluster.getItems();
    }

    public CameraPosition getPosition() {
        return cluster.getPosition();
    }

    public int getSize() {
        return cluster.getItems().size();
    }
}
