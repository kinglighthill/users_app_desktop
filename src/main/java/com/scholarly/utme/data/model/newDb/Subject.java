package com.scholarly.utme.data.model.newDb;

public class Subject {

    private int id;
    private String title;
    private String shortTitle;
    private String description;
    private String colorCode;

    public Subject() {

    }

    public Subject(int id, String title, String shortTitle, String description, String colorCode) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
        this.description = description;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
