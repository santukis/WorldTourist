package com.david.arlocation.view.components;


import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.cluster.model.Cluster;

public interface OnClusterClickListener<T extends ArItem> {

    void onClusterClick(Cluster<T> cluster);
}
