package com.david.arlocation.cluster.model;


import com.david.arlocation.aritems.model.ArItem;

import java.util.Collection;

public interface Cluster <T extends ArItem> {

    CameraPosition getPosition();

    Collection<T> getItems();

    int getSize();
}
