package com.david.worldtourist.items.domain.model;


public class ItemDate {

    private String date = "";
    private String time = "";

    public static ItemDate EMPTY_DATE = new ItemDate();

    public ItemDate() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
