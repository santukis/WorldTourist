package com.david.arlocation.view.components;


import com.david.arlocation.aritems.model.ArItem;

public interface OnArItemClickListener<T extends ArItem> {

    void onClusterItemClick(T clusterItem);

}
