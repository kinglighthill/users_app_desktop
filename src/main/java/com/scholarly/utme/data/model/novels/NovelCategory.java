package com.scholarly.utme.data.model.novels;

public class NovelCategory {
    private int id;
    private String category;
    private String createdAt;

    public NovelCategory(int id, String category, String createdAt) {
        this.id = id;
        this.category = category;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
