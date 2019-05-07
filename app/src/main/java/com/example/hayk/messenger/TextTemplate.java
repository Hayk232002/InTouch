package com.example.hayk.messenger;

public class TextTemplate {
    private String message;
    private int text_type;

    public TextTemplate(String message, int text_type) {
        this.message = message;
        this.text_type = text_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getText_type() {
        return text_type;
    }

    public void setText_type(int text_type) {
        this.text_type = text_type;
    }
}
