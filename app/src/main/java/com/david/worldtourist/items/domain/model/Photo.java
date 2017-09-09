package com.david.worldtourist.items.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Photo implements Serializable {

    private String photo = "";
    private String author = "";

    public final static Photo EMPTY_PHOTO = new Photo("", "");
    public final static List<Photo> EMPTY_PHOTO_LIST = Collections.singletonList(EMPTY_PHOTO);

    //Constructors
    public Photo() {
        //Empty constructor for Firebase data read
    }

    public Photo(String photo, String author) {
        this.photo = photo;
        this.author = author;
    }

    //Getters and Setters
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
