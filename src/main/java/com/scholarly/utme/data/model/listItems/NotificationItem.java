package com.scholarly.utme.data.model.listItems;

public class NotificationItem {
    private Long date;
    private String title;
    private String text;

    public NotificationItem(Long date, String title, String text) {
        this.date = date;
        this.title = title;
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
