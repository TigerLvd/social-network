package com.highload.architect.soc.network.model;

public class DialogUserIdSendPostRequest {
    private String text;

    public DialogUserIdSendPostRequest() {}

    public DialogUserIdSendPostRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


