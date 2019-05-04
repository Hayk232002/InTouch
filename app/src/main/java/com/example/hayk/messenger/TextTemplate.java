package com.example.hayk.messenger;

public class TextTemplate {
    private String message;
    private Boolean is_text_only;

    public TextTemplate(String message, Boolean is_text_only) {
        this.message = message;
        this.is_text_only = is_text_only;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIs_text_only() {
        return is_text_only;
    }

    public void setIs_text_only(Boolean is_text_only) {
        this.is_text_only = is_text_only;
    }
}
