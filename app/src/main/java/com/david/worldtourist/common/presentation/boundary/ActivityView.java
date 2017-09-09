package com.david.worldtourist.common.presentation.boundary;


public interface ActivityView extends BaseView{

    void showUserName(String name);

    void showUserMail(String mail);

    void showUserPhoto(String photoUrl);

    void showSessionState(int stateResource);

    void onUserLogout();
}
