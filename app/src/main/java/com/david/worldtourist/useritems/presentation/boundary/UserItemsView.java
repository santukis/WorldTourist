package com.david.worldtourist.useritems.presentation.boundary;


import com.david.worldtourist.items.presentation.boundary.ItemsView;

public interface UserItemsView extends ItemsView {

    void showDeleteMenu();

    void hideDeleteMenu();
}
