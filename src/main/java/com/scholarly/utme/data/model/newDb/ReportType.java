package com.scholarly.utme.data.model.newDb;

public class ReportType {
    private int id;
    private String emoji;
    private String description;

    public ReportType(int id, String emoji, String description) {
        this.id = id;
        this.emoji = emoji;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }
}
