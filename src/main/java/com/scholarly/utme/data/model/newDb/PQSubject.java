package com.scholarly.utme.data.model.newDb;

public class PQSubject {

    private int id;
    private int subjectId;
    private int minutesAlloted;
    private int order;
    private String title;
    private String shortTitle;
    private String colorCode;


    public PQSubject(int id, int subjectId, int minutesAlloted, int order, String title, String shortTitle, String colorCode) {
        this.id = id;
        this.subjectId = subjectId;
        this.minutesAlloted = minutesAlloted;
        this.order = order;
        this.title = title;
        this.shortTitle = shortTitle;
        this.colorCode = colorCode;
    }

    public PQSubject() {

    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getMinutesAlloted() {
        return minutesAlloted;
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

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setMinutesAlloted(int minutesAlloted) {
        this.minutesAlloted = minutesAlloted;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return title;
    }
}
