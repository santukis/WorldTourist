package com.david.arlocation.cluster.boundary;


import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.aritems.model.GeoLocation;
import com.david.arlocation.cluster.model.Cluster;

import java.util.Collection;
import java.util.Set;

public interface ClusterController<T extends ArItem> {

    void setCurrentLocation(GeoLocation coordinates);

    void setCameraProjectionMatrix(float[] cameraMatrix);

    void setRotationMatrix(float[] rotationMatrix);

    void createClusters(Collection<T> arItems, OnClustered<T> callback);

    void updateClusters(OnClustered<T> callback);


    interface OnClustered<T extends ArItem> {

        void onSuccess(Set<? extends Cluster<T>> clusters);
    }
}
