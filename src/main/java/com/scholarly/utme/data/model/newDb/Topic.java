package com.scholarly.utme.data.model.newDb;

public class Topic {
    private int id;
    private String title;
    private int order;

    public Topic(int id, String title, int order) {
        this.id = id;
        this.title = title;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }
}
