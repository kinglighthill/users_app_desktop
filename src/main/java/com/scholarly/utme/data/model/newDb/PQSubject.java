package com.scholarly.utme.data.model.newDb;

public class PQSubject {

    private int id;
    private int subjectId;
    private int minutesAllotted;
    private int order;
    private String title;
    private String shortTitle;
    private String colorCode;
    private boolean favorite;


    public PQSubject(int id, int subjectId, int minutesAllotted, int order, String title, String shortTitle, String colorCode, boolean favorite) {
        this.id = id;
        this.subjectId = subjectId;
        this.minutesAllotted = minutesAllotted;
        this.order = order;
        this.title = title;
        this.shortTitle = shortTitle;
        this.colorCode = colorCode;
        this.favorite = favorite;
    }

    public PQSubject() {

    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getMinutesAllotted() {
        return minutesAllotted;
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public String toString() {
        return title;
    }
}
