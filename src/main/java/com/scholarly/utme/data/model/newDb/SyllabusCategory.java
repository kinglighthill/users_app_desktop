package com.scholarly.utme.data.model.newDb;

public class SyllabusCategory {
    private int id;
    private String title;
    private int syllabusSubjectId;
    private int order;

    public SyllabusCategory(int id, String title, int syllabusSubjectId, int order) {
        this.id = id;
        this.title = title;
        this.syllabusSubjectId = syllabusSubjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSyllabusSubjectId() {
        return syllabusSubjectId;
    }

    public int getOrder() {
        return order;
    }
}
