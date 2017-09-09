package com.david.worldtourist.rating.domain.model;


import java.util.Locale;

public class Rating {

    private String itemId = "";
    private long ratingSize = 0;
    private double ratingValue = 0;

    public static Rating EMPTY_RATING = new Rating();

    public Rating(){
        //Empty constructor for Firebase data read
    }

    public Rating(String itemId){
        this.itemId = itemId;
    }

    public Rating(String itemId, long ratingSize, double ratingValue){
        this.itemId = itemId;
        this.ratingSize = ratingSize;
        this.ratingValue = ratingValue;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public long getRatingSize() {
        return ratingSize;
    }

    public void setRatingSize(long ratingSize) {
        this.ratingSize = ratingSize;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public float calculateAverageRating(){
        return (float) getRatingValue() / (float) getRatingSize();
    }

    public String convertRatingToText(){
        double average =  getRatingValue() / getRatingSize();
        return String.valueOf(String.format(Locale.getDefault(),
                "%.1f", average) + " (" + String.valueOf(getRatingSize() + ")"));
    }
}
