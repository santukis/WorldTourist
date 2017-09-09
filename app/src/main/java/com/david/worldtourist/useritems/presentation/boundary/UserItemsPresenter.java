package com.david.worldtourist.useritems.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;

public interface UserItemsPresenter<T extends BaseView> extends CachedPresenter<T> {

    void loadUserItems();

    void updateSelectedItems(String itemId);

    void deleteItems();
}
