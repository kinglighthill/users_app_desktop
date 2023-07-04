package com.scholarly.utme.data.model.newDb;

public class NoteSubject {
    private int id;
    private String title;
    private int subjectId;
    private int order;
    private String shortTitle;
    private String colorCode;

    public NoteSubject(int id, String title, int subjectId, int order, String shortTitle, String colorCode) {
        this.id = id;
        this.title = title;
        this.subjectId = subjectId;
        this.order = order;
        this.shortTitle = shortTitle;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getOrder() {
        return order;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getColorCode() {
        return colorCode;
    }
}
