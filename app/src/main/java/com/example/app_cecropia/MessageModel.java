package com.example.app_cecropia;

public class MessageModel {

    String message;
    Integer color;

    public MessageModel(String message, Integer color) {
        this.message=message;
        this.color=color;
    }

    public String getMessage() {
        return message;
    }

    public Integer getColor() {
        return color;
    }
}