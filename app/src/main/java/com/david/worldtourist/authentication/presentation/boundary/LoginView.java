package com.david.worldtourist.authentication.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;

public interface LoginView extends BaseView {

    void showLoadingBar();

    void hideLoadingBar();

    void showErrorMessage(String provider);

    void loadActivity();
}
