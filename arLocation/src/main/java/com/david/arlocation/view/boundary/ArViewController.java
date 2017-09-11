package com.david.arlocation.view.boundary;

import com.david.arlocation.aritems.boundary.ConfigManager;
import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.Cluster;

import java.util.Set;


public interface ArViewController<T extends ArItem> extends ConfigManager<T> {

    void createMarkers(Set<? extends Cluster<T>> clusters);

    void updateMarkersPosition();

    void clearMarkers();
}
