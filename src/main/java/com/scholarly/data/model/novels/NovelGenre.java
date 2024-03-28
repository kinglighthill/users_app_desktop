package com.scholarly.data.model.novels;

public class NovelGenre {
    private int id;
    private String genre;
    private String order;

    public NovelGenre(int id, String genre, String order) {
        this.id = id;
        this.genre = genre;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getOrder() {
        return order;
    }
}
