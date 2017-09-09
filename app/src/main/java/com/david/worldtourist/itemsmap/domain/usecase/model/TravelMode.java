package com.david.worldtourist.itemsmap.domain.usecase.model;


import com.david.worldtourist.R;

public enum TravelMode {

    NONE ("", 0, 0),

    DRIVING ("driving", R.drawable.ic_driving, R.color.colorPrimary),

    WALKING ("walking", R.drawable.ic_walking, R.color.colorAccent);


    private String name;
    private int icon;
    private int color;

    TravelMode(String name, int icon, int color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }
}
