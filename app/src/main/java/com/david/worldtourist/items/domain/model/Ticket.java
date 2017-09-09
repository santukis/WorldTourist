package com.david.worldtourist.items.domain.model;


public class Ticket {

    private String currency = "";
    private double minPrice = 0F;
    private double maxPrice = 0F;

    public static Ticket EMPTY_TICKET = new Ticket();

    public Ticket() {}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
