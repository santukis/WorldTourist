package com.david.arlocation.view.model;


import android.graphics.drawable.Drawable;

public final class MarkerOptions {

    private String title = "";
    private Drawable icon;

    public MarkerOptions(){}

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public MarkerOptions withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public MarkerOptions withTitle(String title) {
        this.title = title;
        return this;
    }
}
