package com.scholarly.utme.data.model.novels;

public class NovelGenre {
    private int id;
    private String genre;
    private String createdAt;

    public NovelGenre(int id, String genre, String createdAt) {
        this.id = id;
        this.genre = genre;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
