package com.scholarly.data.model.novels;

public class NovelCategory {
    private int id;
    private String category;
    private String order;

    public NovelCategory(int id, String category, String order) {
        this.id = id;
        this.category = category;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getOrder() {
        return order;
    }
}

