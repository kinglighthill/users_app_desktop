package com.scholarly.utme.data.model.newDb;

public class SyllabusCategory {
    private int id;
    private String title;
    private int order;

    public SyllabusCategory(int id, String title, int order) {
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

    @Override
    public String toString() {
        return title;
    }
}
