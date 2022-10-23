package com.scholarly.utme.data.model.newDb;

public class PQTopic {
    private int id;
    private String title;
    private int subjectId;
    private String createdAt;

    public PQTopic(int id, String title, int subjectId, String createdAt) {
        this.id = id;
        this.title = title;
        this.subjectId = subjectId;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }


    @Override
    public String toString() {
        return title;
    }
}
