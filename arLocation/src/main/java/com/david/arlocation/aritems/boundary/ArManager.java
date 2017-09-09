package com.david.arlocation.aritems.boundary;

import com.david.arlocation.aritems.model.ArItem;

import java.util.Collection;


public interface ArManager<T extends ArItem> extends ConfigManager<T> {

    void init();

    void release();

    void addArItems(Collection<T> arItems);
}
