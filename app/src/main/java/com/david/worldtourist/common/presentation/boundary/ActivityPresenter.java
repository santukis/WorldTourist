package com.david.worldtourist.common.presentation.boundary;


public interface ActivityPresenter<T extends ActivityView> extends BasePresenter<T> {

    void removeUser(String userId);

    void updateUserSession();
}
