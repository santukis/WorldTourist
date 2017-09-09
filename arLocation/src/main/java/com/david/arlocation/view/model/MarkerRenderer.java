package com.david.arlocation.view.model;


import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.Cluster;

public interface MarkerRenderer<T extends ArItem> {

    void onBeforeItemMarkerRenderer(T item, MarkerOptions markerOptions);

    void onBeforeClusterMarkerRenderer(Cluster<T> cluster, MarkerOptions markerOptions);
}
