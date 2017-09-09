package com.david.worldtourist.items.domain.model;

import android.support.annotation.NonNull;

import java.util.List;

public class Item {

    private String id = "";
    private String name = "";
    private String phone = "";
    private String summary = "";
    private String description = "";
    private String info = "";
    private String city = "";
    private String address = "";
    private String[] timetable = new String[0];
    private GeoCoordinate coordinate = GeoCoordinate.EMPTY_COORDINATE;
    private ItemType type = ItemType.NONE;
    private List<Photo> photos = Photo.EMPTY_PHOTO_LIST;
    private ItemUserFilter filter = ItemUserFilter.NONE;
    private ItemDate startDate = ItemDate.EMPTY_DATE;
    private ItemDate endDate = ItemDate.EMPTY_DATE;
    private Ticket ticket = Ticket.EMPTY_TICKET;

    public final static Item EMPTY_ITEM = new Builder("").build();

    //Constructors
    public Item(){
        //Empty constructor for Firebase data read
    }

    private Item(Item.Builder builder) {
        id = builder.id;
        name = builder.name;
        phone = builder.phone;
        summary = builder.summary;
        description = builder.description;
        info = builder.info;
        city = builder.city;
        address = builder.address;
        timetable = builder.timetable;
        type = builder.type;
        coordinate = builder.sCoordinate;
        photos = builder.photos;
        filter = builder.filter;
        startDate = builder.startDate;
        endDate = builder.endDate;
        ticket = builder.ticket;
    }

    //Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getTimetable() {
        return timetable;
    }

    public void setTimetable(String[] timetable) {
        this.timetable = timetable;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public ItemUserFilter getFilter() {
        return filter;
    }

    public void setFilter(ItemUserFilter filter) {
        this.filter = filter;
    }

    public ItemDate getStartDate() {
        return startDate;
    }

    public void setStartDate(ItemDate startDate) {
        this.startDate = startDate;
    }

    public ItemDate getEndDate() {
        return endDate;
    }

    public void setEndDate(ItemDate endDate) {
        this.endDate = endDate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }


    public static class Builder {

        private String id;
        private String name = "";
        private String phone = "";
        private String summary= "";
        private String description = "";
        private String info = "";
        private String city = "";
        private String address = "";
        private String[] timetable = new String[0];
        private ItemType type = ItemType.NONE;
        private GeoCoordinate sCoordinate = GeoCoordinate.EMPTY_COORDINATE;
        private List<Photo> photos = Photo.EMPTY_PHOTO_LIST;
        private ItemUserFilter filter = ItemUserFilter.NONE;
        private ItemDate startDate = ItemDate.EMPTY_DATE;
        private ItemDate endDate = ItemDate.EMPTY_DATE;
        private Ticket ticket = Ticket.EMPTY_TICKET;

        public Builder(@NonNull String id){
            this.id = id;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withPhone(String phone){
            this.phone = phone;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withDescription(String description){
            this.description = description;
            return this;
        }

        public Builder withInfo(String info){
            this.info = info;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withTimetable(String[] timetable){
            this.timetable = timetable;
            return this;
        }

        public Builder withType(ItemType type){
            this.type = type;
            return this;
        }

        public Builder withCoordinate(GeoCoordinate sCoordinate){
            this.sCoordinate = sCoordinate;
            return this;
        }

        public Builder withPhotos(List<Photo> photos){
            this.photos = photos;
            return this;
        }

        public Builder withFilter(ItemUserFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder withStartDate(ItemDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(ItemDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withTicket(Ticket ticket) {
            this.ticket = ticket;
            return this;
        }

        public Item build(){
            return new Item(this);
        }
    }
}
