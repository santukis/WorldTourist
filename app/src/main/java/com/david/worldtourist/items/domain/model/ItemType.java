package com.david.worldtourist.items.domain.model;

import com.david.worldtourist.R;

public enum ItemType {

    CULTURE(R.string.culture,
            R.drawable.ic_culture,
            R.drawable.ic_culture_ar,
            R.color.color_culture_stroke,
            R.drawable.info_content_culture),

    NATURE(R.string.nature,
            R.drawable.ic_nature,
            R.drawable.ic_nature_ar,
            R.color.color_nature_stroke,
            R.drawable.info_content_nature),

    ENTERTAINMENT(R.string.entertainment,
            R.drawable.ic_entertainment,
            R.drawable.ic_entertainment_ar,
            R.color.color_entertainment_stroke,
            R.drawable.info_content_entertainment),

    GASTRONOMY(R.string.gastronomy,
            R.drawable.ic_gastronomy,
            R.drawable.ic_gastronomy_ar,
            R.color.color_gastronomy_stroke,
            R.drawable.info_content_gastronomy),

    NONE(0, 0, 0, 0, 0);

    private int name;
    private int icon;
    private int arIcon;
    private int color;
    private int background;

    ItemType(int name, int icon, int arIcon, int color, int background){
        this.name = name;
        this.icon = icon;
        this.arIcon = arIcon;
        this.color = color;
        this.background = background;
    }

    public int getName(){
        return name;
    }

    public int getIcon(){
        return icon;
    }

    public int getArIcon(){
        return arIcon;
    }

    public int getColor() {
        return color;
    }

    public int getBackground() {
        return background;
    }
}
