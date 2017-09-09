package com.david.worldtourist.message.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;

public interface MessagePresenter<T extends BaseView> extends CachedPresenter<T> {

    void sendMessage(String messageText, float messageRating);
}
