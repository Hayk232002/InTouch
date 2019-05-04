package com.example.hayk.messenger;

public class UserInformation {
    String downloadUri;
    String uid;
    String username;

    public UserInformation(){

    }

    public UserInformation(String downloadUri, String uid   , String username/*String key*/) {
        this.downloadUri = downloadUri;
        this.uid = uid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}
