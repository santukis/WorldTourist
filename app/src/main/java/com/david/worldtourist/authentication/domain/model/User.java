package com.david.worldtourist.authentication.domain.model;


import java.io.Serializable;

public class User implements Serializable {

    private String id = "";
    private String name = "";
    private String mail = "";
    private String image = "";
    private String provider = "";

    public final static User EMPTY_USER = new User();

    public User(){
        //Empty constructor for Firebase data read
    }

    public User(String id, String name, String mail, String image){
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.image = image;
    }

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
