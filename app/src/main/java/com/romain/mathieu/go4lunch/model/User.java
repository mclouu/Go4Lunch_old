package com.romain.mathieu.go4lunch.model;


import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    private Boolean enebleNotif;
    @Nullable
    private String urlPicture;

    public User() {
    }

    User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.enebleNotif = true;
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public Boolean getEnebleNotif() {
        return enebleNotif;
    }

    // --- SETTERS ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setEnebleNotif(Boolean enebleNotif) {
        this.enebleNotif = enebleNotif;
    }

}