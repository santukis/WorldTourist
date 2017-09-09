package com.david.worldtourist.aritems.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;
import com.david.worldtourist.items.domain.model.LoadingState;

public interface ArItemsPresenter<T extends BaseView> extends CachedPresenter<T> {

    void checkPermissions(final String[] permissions, final int[] requestCodes);

    void loadArItems(LoadingState loadingState);
}
