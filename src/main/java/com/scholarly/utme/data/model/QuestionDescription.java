package com.scholarly.utme.data.model;

public class QuestionDescription {
    private int id;
    private String description;
    private int subjectId;
    private int yearId;

    public QuestionDescription(int id, String description, int subjectId, int yearId) {
        this.id = id;
        this.description = description;
        this.subjectId = subjectId;
        this.yearId = yearId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getYearId() {
        return yearId;
    }

}
