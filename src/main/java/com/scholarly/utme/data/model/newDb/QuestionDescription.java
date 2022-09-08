package com.scholarly.utme.data.model.newDb;

public class QuestionDescription {
    private int id;
    private String description;
    private int subjectId;
    private int yearId;
    private String createdAt;

    public QuestionDescription(int id, String description, int subjectId, int yearId, String createdAt) {
        this.id = id;
        this.description = description;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return description;
    }
}
