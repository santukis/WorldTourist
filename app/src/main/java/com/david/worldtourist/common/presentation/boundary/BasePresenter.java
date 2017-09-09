package com.david.worldtourist.common.presentation.boundary;


import android.support.annotation.NonNull;

public interface BasePresenter<T extends BaseView> {

    void setView(@NonNull T view);

    void onCreate();

    void onStart();

    void onStop();

    void onDestroy();
}
