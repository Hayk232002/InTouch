package com.example.hayk.messenger;

public class TypingModel {
    String text;

    public TypingModel(){

    }

    public TypingModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
