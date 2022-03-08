package com.mertcikendin.mertcikendinfinal.models;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String photo;
    private String namelastname;
    private String email;
    private String password;

    public User() {
    }

    public User(String uid, String photo, String namelastname, String email, String password) {
        this.uid = uid;
        this.photo = photo;
        this.namelastname = namelastname;
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNamelastname() {
        return namelastname;
    }

    public void setNamelastname(String namelastname) {
        this.namelastname = namelastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
