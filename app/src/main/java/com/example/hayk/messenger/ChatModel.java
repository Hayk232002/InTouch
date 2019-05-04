package com.example.hayk.messenger;

public class ChatModel {
    String fromid;
    String text;
    String toid;


    public ChatModel(){

    }

    public ChatModel(String fromid, String text, String toid) {
        this.fromid = fromid;
        this.text = text;
        this.toid = toid;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
