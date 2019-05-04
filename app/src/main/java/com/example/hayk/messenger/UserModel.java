package com.example.hayk.messenger;

public class UserModel {

    String uid;
    String username;

    public UserModel(){

    }

    public UserModel(String uid,String username ) {
        this.uid = uid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setKey(String key) {
        this.uid= key;
    }
}
