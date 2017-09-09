package com.david.worldtourist.items.domain.model;


import com.david.worldtourist.R;

public enum ItemCategory {

    NONE(0, 0, 0, 0),

    SITE(
            R.string.menu_sites,
            R.string.menu_site,
            R.string.no_closest_sites,
            R.drawable.ic_no_closest_sites),

    EVENT(
            R.string.menu_events,
            R.string.menu_event,
            R.string.no_closest_events,
            R.drawable.ic_no_closest_events);


    private int pluralName;

    private int singleName;

    private int noItemsText;

    private int itemIcon;

    ItemCategory(int pluralName,
                 int singleName,
                 int noItemsText,
                 int itemIcon) {

        this.pluralName = pluralName;
        this.singleName = singleName;
        this.noItemsText = noItemsText;
        this.itemIcon = itemIcon;
    }

    public int getPluralName() {
        return pluralName;
    }

    public int getSingleName() {
        return singleName;
    }

    public int getNoItemsText() {
        return noItemsText;
    }

    public int getItemIcon() {
        return itemIcon;
    }

}
