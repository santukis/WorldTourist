package com.david.worldtourist.message.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.FragmentView;

public interface MessageView extends FragmentView {

    void showUserName(String name);

    void showUserPhoto(String photoUrl);

    void showMessageRating(float rating);

    void showMessageText(String text);

    void enableButtons();

    void disableButtons();
}
