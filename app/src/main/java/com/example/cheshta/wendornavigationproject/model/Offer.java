package com.example.cheshta.wendornavigationproject.model;

public class Offer {
    public String title, desc;

    public Offer() {
    }

    public Offer(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
