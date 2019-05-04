package com.example.hayk.messenger;

public class ProfileModel {
    String downloadUri;
    String fromid;
    String key;
    String text;
    String toid;
    String username;


    public ProfileModel(){

    }

    public ProfileModel(String downloadUri, String fromid, String key, String text, String toid, String username) {
        this.downloadUri = downloadUri;
        this.fromid = fromid;
        this.key = key;
        this.text = text;
        this.toid = toid;
        this.username = username;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
