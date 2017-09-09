package com.david.worldtourist.message.domain.model;


import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.authentication.domain.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private String itemId = "";
    private String messageId = "";
    private User user = User.EMPTY_USER;
    private String text = "";
    private long date = 0;
    private float userRating = 0;
    private int likes = 0;
    private List<String> userLikesIds = new ArrayList<>();
    private List<Photo> photos = Photo.EMPTY_PHOTO_LIST;

    public static final Message MESSAGE_EMPTY =  new Message.Builder().build();

    public Message(){
        //Empty constructor for Firebase data read
    }

    private Message(Message.Builder builder){
        this.itemId = builder.itemId;
        this.messageId = builder.messageId;
        this.user = builder.user;
        this.text = builder.text;
        this.date = builder.date;
        this.userRating = builder.userRating;
        this.likes = builder.likes;
        this.userLikesIds = builder.userLikesIds;
        this.photos = builder.photos;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getUserLikesIds() {
        return userLikesIds;
    }

    public void setUserLikesIds(List<String> userLikesIds) {
        this.userLikesIds = userLikesIds;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public static class Builder {

        private String itemId = "";
        private String messageId = "";
        private User user = User.EMPTY_USER;
        private String text = "";
        private long date = 0;
        private float userRating = 0f;
        private int likes = 0;
        private List<String> userLikesIds = new ArrayList<>();
        private List<Photo> photos = Photo.EMPTY_PHOTO_LIST;

        public Builder withItemId(String itemId){
            this.itemId = itemId;
            return this;
        }

        public Builder withId(String id){
            this.messageId = id;
            return this;
        }

        public Builder withUser(User user){
            this.user = user;
            return this;
        }

        public Builder withText(String text){
            this.text = text;
            return this;
        }

        public Builder withDate(long date){
            this.date = date;
            return this;
        }

        public Builder withRating(float rating){
            userRating = rating;
            return this;
        }

        public Builder withLikes(int likes){
            this.likes = likes;
            return this;
        }

        public Builder withUserLikesIds(List<String> userLikesIds){
            this.userLikesIds = userLikesIds;
            return this;
        }

        public Builder withPhotos(List<Photo> photos) {
            this.photos = photos;
            return this;
        }

        public Message build(){
            return new Message(this);
        }
    }
}
