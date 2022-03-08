package com.mertcikendin.mertcikendinfinal.models;

public class Message {
    private String message;
    private String from;
    private String to;
    private String datetime;

    public Message() {
    }

    public Message(String message, String from, String to, String datetime) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}