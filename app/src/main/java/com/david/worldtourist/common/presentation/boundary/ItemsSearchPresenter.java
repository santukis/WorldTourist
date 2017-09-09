package com.david.worldtourist.common.presentation.boundary;


public interface ItemsSearchPresenter<T extends BaseView> extends CachedPresenter<T> {

    void startItemsSearch(String text);

    void stopItemsSearch();
}
