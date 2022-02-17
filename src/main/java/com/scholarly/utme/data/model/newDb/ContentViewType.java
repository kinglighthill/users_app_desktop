package com.scholarly.utme.data.model.newDb;

public class ContentViewType {
    private int id;
    private String text;

    public ContentViewType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
